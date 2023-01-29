package com.zerobase.everycampingbackend.domain.order.entity;

import com.zerobase.everycampingbackend.common.BaseEntity;
import com.zerobase.everycampingbackend.domain.order.type.OrderStatus;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.settlement.entity.DailySettlement;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dailySettlement_id")
    private DailySettlement dailySettlement;

    Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Integer amount; //총액

    private String productNameSnapshot; // 주문 시 상품명
    private Integer stockPriceSnapshot; // 주문 시 개당 가격
    private String imageUriSnapshot; // 주문 시 이미지

    private LocalDateTime confirmedAt; // 주문확정 일자

    public static OrderProduct of(Orders orders, Product product, Integer quantity) {

        return OrderProduct.builder()
            .orders(orders)
            .product(product)
            .status(OrderStatus.COMPLETE)
            .quantity(quantity)
            .amount(quantity * product.getPrice())
            .productNameSnapshot(product.getName())
            .stockPriceSnapshot(product.getPrice())
            .imageUriSnapshot(product.getImageUri())
            .build();
    }
}
