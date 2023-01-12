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
public class OrderForm {
  private Long customerId; //임시
  private List<OrderProductForm> orderProductFormList;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderProductForm {
    private Long productId;
    private Integer quantity;
  }
}
