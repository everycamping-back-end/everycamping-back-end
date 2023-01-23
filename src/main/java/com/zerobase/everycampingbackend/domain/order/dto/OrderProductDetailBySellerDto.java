package com.zerobase.everycampingbackend.domain.order.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.order.type.OrderStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDetailBySellerDto {

    //주문상품 관련정보
    private Long orderProductId;
    private Long productId;
    private String productNameSnapshot; // 주문 시 상품명
    private Integer stockPriceSnapshot; // 주문 시 개당 가격
    private String imageUriSnapshot; // 주문 시 상품 이미지 url
    private Integer quantity; //주문수량
    private Integer amount; // 총 금액(구매자가 지불한 금액)

    //수령인 정보
    private String receiverName; //수령자 이름
    private String receiverAddress; //배송지 주소
    private String receiverPhone; //수령인 전화번호
    private String request; //주문 요청사항

    //구매자 정보
    private Long customerId;
    private String customerEmail;
    private String customerNickName;
    private String customerPhone;

    private Long sellerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 주문 일자
    private OrderStatus status; //주문 상태
}
