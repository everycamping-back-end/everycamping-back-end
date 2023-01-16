package com.zerobase.everycampingbackend.common.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ExceptionResponse> customRequestException(final CustomException ex) {
    log.error("api Exception 발생: {}\nstackTrace : {}", ex.getErrorCode(), ex.getStackTrace());
    return new ResponseEntity<>(
        new ExceptionResponse(ex.getMessage(), ex.getErrorCode()),
        ex.getErrorCode().getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BindingExceptionResponse> argumentNotValidException(
      final MethodArgumentNotValidException ex) {
    log.error("argumentNotValidException 발생\nmessage : {}\nstackTrace :{}", ex.getMessage(),
        ex.getStackTrace());
    return new ResponseEntity<>(
        new BindingExceptionResponse(ErrorCode.ARGUMENT_NOT_VALID_EXCEPTION.getDetail(),
            ErrorCode.ARGUMENT_NOT_VALID_EXCEPTION, ex.getBindingResult()),
        ErrorCode.ARGUMENT_NOT_VALID_EXCEPTION.getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> unknownException(final Exception ex) {
    log.error("unknown Exception 발생\nmessage : {}\nstackTrace : {}",
        ex.getMessage(), ex.getStackTrace());
    return new ResponseEntity<>(
        new ExceptionResponse(ex.getMessage(), ErrorCode.UNKNOWN_EXCEPTION),
        ErrorCode.UNKNOWN_EXCEPTION.getHttpStatus());
  }

  @Getter
  @AllArgsConstructor
  public static class ExceptionResponse {

    private String message;
    private ErrorCode errorCode;
  }

  @Getter
  public static class BindingExceptionResponse extends ExceptionResponse {

    List<CustomFieldError> fieldErrorList;

    public BindingExceptionResponse(String message, ErrorCode errorCode,
        BindingResult bindingResult) {
      super(message, errorCode);

      this.fieldErrorList = CustomFieldError.from(bindingResult);
    }

    @Getter
    @AllArgsConstructor
    public static class CustomFieldError {

      private String field;
      private Object rejectedValue;
      private String reason;

      public static List<CustomFieldError> from(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
            .map(error -> new CustomFieldError(
                error.getField(),
                error.getRejectedValue() == null ?
                    "" : error.getRejectedValue().toString(),
                error.getDefaultMessage()))
            .collect(Collectors.toList());
      }
    }
  }
}
