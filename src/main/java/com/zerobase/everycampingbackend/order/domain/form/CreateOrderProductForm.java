package com.zerobase.everycampingbackend.order.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderProductForm {

  private Long productId;
  private Integer count;
  private Integer partialAmount;
}
