package com.zerobase.everycampingbackend.domain.order.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.order.type.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
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
public class OrderDetailByCustomerDto {

    List<OrderProductDto> orderProductList; //주문 품목리스트

    private Long orderId; //pk
    private String representProductName; //대표 주문상품
    private Integer orderProductCount; //주문상품 (종류)개수
    private Integer totalAmount; //총 금액

    private String name; //수령인 이름
    private String address; //배송지 주소
    private String phone; //수령자 전화번호
    private String request; //주문 요청사항

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 주문 일자



    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductDto {
        private Long id;

        //상품 관련정보
        private Long productId;

        //스냅샷
        private String productNameSnapshot; // 주문 시 상품명
        private Integer stockPriceSnapshot; // 주문 시 개당 가격
        private Integer amount; //주문 시 금액
        private String imageUriSnapshot; // 주문 시 상품 이미지 url

        //주문 관련정보
        private Integer quantity; //주문수량
        private OrderStatus status; //주문 상태

        //판매자 관련정보
        private Long sellerId;
        private String sellerNickName;
        private String sellerEmail;
        private String sellerPhone;
    }
}
