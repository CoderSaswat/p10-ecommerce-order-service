package com.ecommerce.order.exception;

import com.ecommerce.order.dto.ResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Order( Ordered.HIGHEST_PRECEDENCE )
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {

  @ExceptionHandler( ENFException.class )
  @ResponseStatus( HttpStatus.BAD_REQUEST )
  public ResponseDto handleENFException(final ENFException e ) {
    return entityNotFoundHandler( e.getMessage(), String.valueOf( e.getCode() ) );
  }

  @ExceptionHandler( BusinessException.class )
  @ResponseStatus( HttpStatus.BAD_REQUEST )
  public ResponseDto handleCustomException(final BusinessException e ) {
    return businessExceptionHandler( e.getMessage(), String.valueOf( e.getCode() ) );
  }

  @ExceptionHandler( MethodArgumentNotValidException.class )
  @ResponseStatus( HttpStatus.BAD_REQUEST )
  public ResponseEntity<ResponseDto> handleValidationException(final MethodArgumentNotValidException ex ) {
    // Log the exception
    log.warn( "[WARN {}] Invalid Method argument occurred, details: {}", LocalDateTime.now(), ex.getStackTrace() );

    // Build the error response
    final String errorMessage = "Invalid value entry";
    final String errorCode = "VALIDATION_ERROR";

    // Extract incorrect fields and values
    final List<String> incorrectFields = new ArrayList<>();
    final List<String> incorrectValues = new ArrayList<>();
    for( final FieldError fieldError : ex.getBindingResult().getFieldErrors() ) {
      incorrectFields.add( fieldError.getField() );
      incorrectValues.add( fieldError.getDefaultMessage() );
    }

    final ResponseDto responseDto = new ResponseDto( errorMessage, errorCode, incorrectFields, incorrectValues );

    return ResponseEntity.badRequest().body( responseDto );
  }

  @ExceptionHandler( ConstraintViolationException.class )
  @ResponseStatus( HttpStatus.BAD_REQUEST )
  public ResponseEntity<ResponseDto> handleConstraintViolationException( final ConstraintViolationException ex ) {
    // Build the error response
    final String errorMessage = "Invalid value entry";
    final String errorCode = "ERR_CONSTRAINT_VIOLATION";

    final List<String> incorrectFields = new ArrayList<>();
    final List<String> incorrectValues = new ArrayList<>();
    for( final ConstraintViolation<?> violation : ex.getConstraintViolations() ) {
      incorrectFields.add( violation.getPropertyPath().toString() );
      incorrectValues.add( violation.getMessage() );
    }
    final ResponseDto responseDto = new ResponseDto( errorMessage, errorCode, incorrectFields, incorrectValues );
    return new ResponseEntity<>( responseDto, HttpStatus.BAD_REQUEST );
  }

  private ResponseDto entityNotFoundHandler( final String message, final String code ) {
    return new ResponseDto( message, code, null, null );
  }

  private ResponseDto businessExceptionHandler( final String message, final String code ) {
    return new ResponseDto( message, code, null, null );
  }
}
