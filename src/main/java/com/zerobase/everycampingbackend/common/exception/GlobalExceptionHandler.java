package com.zerobase.everycampingbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ExceptionResponse> customRequestException(final CustomException ex) {
    log.error("api Exception : {} \nstackTrace : {}", ex.getErrorCode(), ex.getStackTrace());
    return new ResponseEntity<>(
        new ExceptionResponse(ex.getMessage(), ex.getErrorCode()),
        ex.getErrorCode().getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> unknownException(final Exception ex) {
    log.error("unknown Exception : {},\nmessage : {} \nstackTrace : {}",
        ErrorCode.UNKNOWN_EXCEPTION,
        ex.getMessage(), ex.getStackTrace());
    return new ResponseEntity<>(
        new ExceptionResponse(ex.getMessage(), ErrorCode.UNKNOWN_EXCEPTION),
        ErrorCode.UNKNOWN_EXCEPTION.getHttpStatus());
  }

  @Getter
  @ToString
  @AllArgsConstructor
  public static class ExceptionResponse {

    private String message;
    private ErrorCode errorCode;
  }
}
