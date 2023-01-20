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
public class OrderProductBySellerDto {

    //스냅샷
    private String productNameSnapshot; // 주문 시 상품명
    private Integer stockPriceSnapshot; // 주문 시 개당 가격
    private String imageUriSnapshot; // 주문 시 상품 이미지 url

    //상품 관련정보
    private Long productId;

    //주문 관련정보
    private Long orderProductId;
    private String address; //배송지 주소
    private String phone; //수령인 전화번호
    private Integer quantity; //주문수량
    private Integer amount; // 총 금액(구매자가 지불한 금액)
    private OrderStatus status; //주문 상태

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 주문 일자

    //구매자 관련 정보
    private Long customerId;
}
