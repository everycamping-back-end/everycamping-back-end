package com.zerobase.everycampingbackend.domain.cart.form;

import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartForm {

    @Min(1)
    private Integer quantity;
}
