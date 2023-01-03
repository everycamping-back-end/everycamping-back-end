package com.zerobase.everycampingbackend.order.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.zerobase.everycampingbackend.order.domain.model.Order;
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

	public static OrderDto from(Order order) {
		List<OrderProductDto> orderProductList = order.getOrderProductList()
			.stream().map(e -> OrderProductDto.from(e)).collect(Collectors.toList());

		return OrderDto.builder()
						.id(order.getId())
						.status(order.getStatus())
						.amount(order.getAmount())
						.orderProductList(orderProductList)
						.build();
	}
}
