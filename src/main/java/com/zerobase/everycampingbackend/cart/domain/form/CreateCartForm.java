package com.zerobase.everycampingbackend.cart.domain.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartForm {

  @NotNull
  Long customerId;

  @Min(1)
  Integer quantity;
}
