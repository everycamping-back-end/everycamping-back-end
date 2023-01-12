package com.zerobase.everycampingbackend.order.domain.entity;

import com.zerobase.everycampingbackend.common.BaseEntity;
import com.zerobase.everycampingbackend.order.type.OrderStatus;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
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
  @JoinColumn(name = "Orders_id")
  private Orders orders;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "settlement_id")
//  private Settlement settlement;

  Integer quantity;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private Integer amount;

  public static OrderProduct of(Orders orders, Product product, Integer quantity) {

    return OrderProduct.builder()
        .orders(orders)
        .product(product)
        .status(OrderStatus.COMPLETE)
        .quantity(quantity)
        .amount(quantity * product.getPrice())
        .build();
  }
}
