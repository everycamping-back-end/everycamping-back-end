package com.zerobase.everycampingbackend.cart.controller;

import com.zerobase.everycampingbackend.cart.domain.dto.CartProductDto;
import com.zerobase.everycampingbackend.cart.domain.form.CreateCartForm;
import com.zerobase.everycampingbackend.cart.domain.form.UpdateQuantityForm;
import com.zerobase.everycampingbackend.cart.service.CartService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  /**
   * 장바구니에 물건 추가 원래는 token을 받아서, 토큰정보를 통해 로그인한 유저의 정보를 알아낸 다음,
   * 해당 유저의 장바구니에 상품을 추가해야 한다. 현재
   * token을받아서 사용하는 방법이 정립되지 않았기 때문에 임시로 Customer_id(pk)를 받도록 구현하였음.
   */
  @PostMapping("/add/{productId}")
  public ResponseEntity createCarts(@RequestBody @Valid CreateCartForm form,
      @PathVariable Long productId) {

    cartService.createCart(form, productId);
    return ResponseEntity.ok().build();
  }

  /**
   * 원래는 토큰을 받아야 하지만, 임시로 customerId를 받는다.
   */
  @GetMapping
  public ResponseEntity<Page<CartProductDto>> getCartProductList(
      @RequestParam @Valid @NotNull Long customerId,
      Pageable pageable) {

    return ResponseEntity.ok(cartService.getCartProductList(customerId, pageable));
  }

  @PatchMapping("/{productId}")
  public ResponseEntity updateQuantity(@RequestBody @Valid UpdateQuantityForm updateQuantityForm,
      @PathVariable Long productId) {

    cartService.updateQuantity(productId, updateQuantityForm);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity deleteCartProduct(@RequestBody @NotNull Long customerId,
      @PathVariable Long productId) {

    cartService.deleteCartProduct(productId, customerId);
    return ResponseEntity.ok().build();
  }
}
