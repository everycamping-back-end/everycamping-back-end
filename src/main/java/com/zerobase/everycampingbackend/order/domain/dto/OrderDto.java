package com.zerobase.everycampingbackend.order.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.zerobase.everycampingbackend.order.domain.model.Orders;
import com.zerobase.everycampingbackend.order.type.OrderStatus;

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
public class OrderDto {

  private Long id;
  private OrderStatus status;
  private Integer amount;
  private List<OrderProductDto> orderProductList;

  public static OrderDto from(Orders orders) {
    List<OrderProductDto> orderProductList = orders.getOrderProductList()
        .stream().map(e -> OrderProductDto.from(e)).collect(Collectors.toList());

    return OrderDto.builder()
        .id(orders.getId())
        .status(orders.getStatus())
        .amount(orders.getAmount())
        .orderProductList(orderProductList)
        .build();
  }
}
