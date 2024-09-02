package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.enums.ResponseCodes;
import com.ecommerce.order.exception.ENFException;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.order.serviceclient.InventoryServiceClient;
import com.ecommerce.order.serviceclient.NotificationServiceClient;
import com.ecommerce.order.serviceclient.ProductServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductServiceClient productServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    @Value("${email}")
    private String email;

    @Value("${password}")
    private String password;



    @Override
    public OrderOutput createOrder(OrderInput orderInput) {
        // Implement business logic to create an order
        Order order = modelMapper.map(orderInput, Order.class);
        order.setOrderDate(LocalDateTime.now());
        order.setId(UUID.randomUUID());
        // Save the order to the repository
//        sendNotification(savedOrder.getUserId());
        checkProductAvailabilityAndPlaceOrder(orderInput.getOrderItems());
        Order savedOrder = orderRepository.save(order);
        sendNotifications(savedOrder.getUserId());
        return modelMapper.map(savedOrder, OrderOutput.class);
    }

    private void checkProductAvailabilityAndPlaceOrder(List<OrderItemInput> orderItems) {
        List<UUID> productIdList = orderItems.stream().map(OrderItemInput::getProductId).toList();
        List<ProductDto> productDtoList = productServiceClient.getProductsByIds(productIdList);
        Map<UUID, String> productMap = productDtoList.stream()
                .collect(Collectors.toMap(ProductDto::getId, ProductDto::getName));

        orderItems.forEach(orderItemInput -> {
            String productName = productMap.get(orderItemInput.getProductId());
            ProductAvailabilityDto productAvailabilityDto = new ProductAvailabilityDto();
            productAvailabilityDto.setProductName(productName);
            productAvailabilityDto.setAmountSold(orderItemInput.getQuantity());
            inventoryServiceClient.updateProductAvailability(productAvailabilityDto);
        });
    }

    private void sendNotifications(UUID userId) {
        sendInAppNotification(userId);
    }

    private void sendInAppNotification(UUID userId) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUserId(userId);
        notificationDto.setMessage("your order has been placed");
        notificationServiceClient.createNotification(notificationDto);
    }

    private void sendNotification(UUID userId) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(email, password);
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> awsCredentials)
                .build();

        String queueName = "q_notification";
        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();

        GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);
        String queueUrl = getQueueUrlResponse.queueUrl();

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("you order has been placed");
        messageDto.setUserId(userId);


        ObjectMapper objectMapper = new ObjectMapper();
        String messageBody;
        try {
            messageBody = objectMapper.writeValueAsString(messageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing MessageDto to JSON", e);
        }

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        sqsClient.sendMessage(sendMessageRequest);

        log.info("message pushed to the queue");

    }

    @Override
    public OrderOutput getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ENFException(Order.class,"id",orderId, ResponseCodes.ENTITY_NOT_FOUND));
        Set<UUID> productIdsSet = order.getOrderItems().stream().map(orderItem -> orderItem.getProductId()).collect(Collectors.toSet());
        List<UUID> productIdsList = new ArrayList<>(productIdsSet);
        List<ProductDto> productDtoList = productServiceClient.getProductsByIds(productIdsList);
        Map<UUID, ProductDto> productDtoMap = productDtoList.stream().collect(Collectors.toMap(ProductDto::getId, productDto -> productDto));

        List<OrderItemOutput> orderItemOutputList = order.getOrderItems().stream().map(orderItem -> {
            ProductDto productDto = productDtoMap.get(orderItem.getProductId());
            OrderItemOutput orderItemOutput = modelMapper.map(orderItem, OrderItemOutput.class);
            orderItemOutput.setDescription(productDto.getDescription());
            orderItemOutput.setName(productDto.getName());
            return orderItemOutput;
        }).toList();

        OrderOutput orderOutput = modelMapper.map(order, OrderOutput.class);
        orderOutput.setOrderItems(orderItemOutputList);
        return orderOutput;
    }

    @Override
    public List<OrderOutput> getOrdersByUser(UUID userId) {

        List<Order> userOrders = orderRepository.findByUserId(userId);

        Set<UUID> productIdsSet = userOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());
        List<UUID> productIdsList = new ArrayList<>(productIdsSet);

        List<ProductDto> productDtoList = productServiceClient.getProductsByIds(productIdsList);
        Map<UUID, ProductDto> productDtoMap = productDtoList.stream()
                .collect(Collectors.toMap(ProductDto::getId, productDto -> productDto));

        return userOrders.stream()
                .map(order -> {
                    List<OrderItemOutput> orderItemOutputs = order.getOrderItems().stream()
                            .map(orderItem -> {
                                ProductDto productDto = productDtoMap.get(orderItem.getProductId());
                                OrderItemOutput orderItemOutput = modelMapper.map(orderItem, OrderItemOutput.class);
                                orderItemOutput.setName(productDto.getName());
                                orderItemOutput.setDescription(productDto.getDescription());
                                return orderItemOutput;
                            })
                            .collect(Collectors.toList());

                    OrderOutput orderOutput = modelMapper.map(order, OrderOutput.class);
                    orderOutput.setOrderItems(orderItemOutputs);
                    return orderOutput;
                })
                .collect(Collectors.toList());
    }
}
