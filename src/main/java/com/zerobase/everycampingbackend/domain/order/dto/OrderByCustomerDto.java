package com.zerobase.everycampingbackend.domain.order.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.everycampingbackend.domain.order.entity.Orders;
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
public class OrderByCustomerDto {

    private Long id; //pk
    private String representProductName; //대표 주문상품
    private Integer orderProductCount; //주문상품 개수
    private Integer totalAmount; //총 금액

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 주문 일자

    public static OrderByCustomerDto from(Orders orders) {

        return OrderByCustomerDto.builder()
            .id(orders.getId())
            .representProductName(orders.getRepresentProductName())
            .orderProductCount(orders.getOrderProductCount())
            .totalAmount(orders.getTotalAmount())
            .createdAt(orders.getCreatedAt())
            .build();
    }
}
