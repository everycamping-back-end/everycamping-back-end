package com.zerobase.everycampingbackend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 예외가 발생했습니다."),
  ARGUMENT_NOT_VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "필드 값이 올바르지 않습니다."),

  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "일치하는 정보가 없습니다."),
  CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 고객입니다."),


  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
  PRODUCT_SELLER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "상품 수정/삭제 권한이 없습니다."),
  PRODUCT_NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다."),
  PRODUCT_NOT_ON_SALE(HttpStatus.BAD_REQUEST, "상품이 판매 중이 아닙니다.")

  ;


  private final HttpStatus httpStatus;
  private final String detail;
}
