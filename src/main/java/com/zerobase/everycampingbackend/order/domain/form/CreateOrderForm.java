package com.zerobase.everycampingbackend.order.domain.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderForm {

  private List<CreateOrderProductForm> orderProducts;
}
