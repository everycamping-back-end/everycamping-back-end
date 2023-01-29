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

    private Long orderProductId;
    private Long productId; //상품 아이디
    private String productNameSnapshot; // 주문 시 상품명
    private Integer stockPriceSnapshot; // 주문 시 개당 가격
    private Integer quantity; //주문수량
    private Integer amount; // 총 금액(구매자가 지불한 금액)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 주문 일자
    private OrderStatus status; //주문 상태
}
