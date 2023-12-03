package com.ecommerce.order.exception;

import com.ecommerce.order.enums.ResponseCodes;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ENFException extends RuntimeException {
  private final ResponseCodes code;

  public ENFException( final Class<?> entity, final UUID entityId, final ResponseCodes code ) {
    this( entity, "id", entityId.toString(), code );
  }

  public ENFException( final Class<?> entity, final String key, final Object value, final ResponseCodes code ) {
    this( "The requested " + entity.getSimpleName() + " with " + key + ": " + value + " doesn't exist.", code );
  }

  public ENFException( final String message, final ResponseCodes code ) {
    super( message );
    this.code = code;
  }

}
