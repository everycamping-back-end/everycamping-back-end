package com.zerobase.everycampingbackend.domain.order.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.order.entity.OrderProduct;
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
public class OrderProductByCustomerDto {
    //상품 관련정보
    private Long productId;

    //스냅샷
    private String productNameSnapshot; // 주문 시 상품명
    private Integer stockPriceSnapshot; // 주문 시 개당 가격
    private String imageUriSnapshot; // 주문 시 상품 이미지 url

    //주문 관련정보
    private Long orderProductId;
    private Integer quantity; //주문수량
    private Integer amount; // 총 금액
    private OrderStatus status; //주문 상태

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 주문 일자

    public static OrderProductByCustomerDto from(OrderProduct orderProduct) {

        return OrderProductByCustomerDto.builder()
            .productId(orderProduct.getProduct().getId())
            .productNameSnapshot(orderProduct.getProductNameSnapshot())
            .stockPriceSnapshot(orderProduct.getStockPriceSnapshot())
            .imageUriSnapshot(orderProduct.getImageUriSnapshot())
            .orderProductId(orderProduct.getId())
            .quantity(orderProduct.getQuantity())
            .amount(orderProduct.getAmount())
            .status(orderProduct.getStatus())
            .createdAt(orderProduct.getCreatedAt())
            .build();
    }
}
