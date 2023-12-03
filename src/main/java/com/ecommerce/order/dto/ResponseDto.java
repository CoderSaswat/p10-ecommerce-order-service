package com.ecommerce.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude( Include.NON_NULL )
public class ResponseDto {

  private String message;
  private String code;
  private List<String> incorrectValues;
  private List<String> incorrectFields;

  public ResponseDto(final String message, final String code, final List<String> incorrectFields,
                     final List<String> incorrectValues ) {
    this.message = message;
    this.code = code;
    this.incorrectValues = incorrectValues;
    this.incorrectFields = incorrectFields;
  }

}
