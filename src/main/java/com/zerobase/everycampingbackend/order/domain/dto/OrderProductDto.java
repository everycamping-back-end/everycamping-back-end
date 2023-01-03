package com.zerobase.everycampingbackend.order.domain.dto;

import com.zerobase.everycampingbackend.order.domain.model.OrderProduct;

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
public class OrderProductDto {
	private Long id;
	private Integer count;
	private Integer partialAmount;

	public static OrderProductDto from(OrderProduct orderProduct) {
		return OrderProductDto.builder()
							.id(orderProduct.getId())
							.count(orderProduct.getCount())
							.partialAmount(orderProduct.getPartialAmount())
							.build();
	}
}
