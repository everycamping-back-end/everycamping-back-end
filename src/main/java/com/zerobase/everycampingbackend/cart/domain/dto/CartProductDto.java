package com.zerobase.everycampingbackend.cart.domain.dto;

import com.zerobase.everycampingbackend.cart.domain.entity.CartProduct;
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
public class CartProductDto {

  private Long productId; //상품 id
  private String name; //상품 이름
  private String imagePath;
  private Integer price; //상품 가격
  private Boolean onSale; //판매여부
  private Integer quantity; //장바구니 등록 수량
  Boolean isQuantityChanged; //quantity 변경 여부(재고부족 등으로 인하여 등록 수량이 변했는지 여부)

  public static CartProductDto of(CartProduct cartProduct, Boolean isCountChanged) {
    return CartProductDto.builder()
        .productId(cartProduct.getProduct().getId())
        .name(cartProduct.getProduct().getName())
        .imagePath(cartProduct.getProduct().getImagePath())
        .price(cartProduct.getProduct().getPrice())
        .onSale(cartProduct.getProduct().isOnSale())
        .quantity(cartProduct.getQuantity())
        .isQuantityChanged(isCountChanged)
        .build();
  }
}
