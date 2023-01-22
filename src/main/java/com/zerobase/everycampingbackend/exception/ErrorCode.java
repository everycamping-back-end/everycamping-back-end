package com.zerobase.everycampingbackend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 예외가 발생했습니다."),
  ARGUMENT_NOT_VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "필드 값이 올바르지 않습니다."),

  CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 들어있지 않은 상품입니다."),
  CART_PRODUCT_ALREADY_ADDED(HttpStatus.BAD_REQUEST, "이미 장바구니에 등록된 상품입니다."),

  ORDER_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
  ORDER_ALREADY_CONFIRMED_OR_CANCELED(HttpStatus.BAD_REQUEST, "이미 확정/취소된 주문입니다."),
  ORDER_CHANGE_STATUS_NOT_AUTHORISED(HttpStatus.UNAUTHORIZED, "확정/취소 권한이 없습니다."),


  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "일치하는 정보가 없습니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
  EMAIL_BEING_USED(HttpStatus.BAD_REQUEST, "사용중인 이메일입니다."),
  USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다."),


  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
  PRODUCT_SELLER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "상품 수정/삭제 권한이 없습니다."),
  PRODUCT_NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다."),
  PRODUCT_NOT_ON_SALE(HttpStatus.BAD_REQUEST, "상품이 판매 중이 아닙니다."),

  REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰를 찾을 수 없습니다."),
  REVIEW_WRITER_NOT_QUALIFIED(HttpStatus.BAD_REQUEST, "리뷰 작성은 해당 상품을 구매한 고객만 가능합니다."),
  REVIEW_EDITOR_NOT_MATCHED(HttpStatus.BAD_REQUEST, "리뷰 수정/삭제 권한이 없습니다."),

  TOKEN_NOT_VALID(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다."),
  TOKEN_STILL_ALIVE(HttpStatus.BAD_REQUEST, "재발급 대상 토큰이 아닙니다."),
  TOKEN_NOT_ALIVE(HttpStatus.BAD_REQUEST, "재발급 대상 토큰입니다."),



  AUTH_CODE_NOT_VALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증코드입니다."),

  WRONG_OAUTH2_PROVIDER(HttpStatus.BAD_REQUEST, "잘못된 소셜로그인 공급자입니다."),

  ;


  private final HttpStatus httpStatus;
  private final String detail;
}
