package com.zerobase.everycampingbackend.order.domain.dto;


import com.zerobase.everycampingbackend.order.type.OrderStatus;
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

    //상품 관련정보
    private Long productId;
    private String productName;
    private Integer stockPrice;
    private String imagePath;

    //주문 관련정보
    private Long orderProductId;
    private Integer quantity;
    private Integer amount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    //구매자 관련 정보
    private Long customerId;
    private String customerNickName;
}
