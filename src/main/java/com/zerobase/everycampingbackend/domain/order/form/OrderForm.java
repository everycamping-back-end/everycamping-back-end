package com.zerobase.everycampingbackend.domain.order.form;

import java.util.List;
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
public class OrderForm {

    private List<OrderProductForm> orderProductFormList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductForm {

        @NotNull
        private Long productId;
        @Min(1)
        private Integer quantity;
    }
}
