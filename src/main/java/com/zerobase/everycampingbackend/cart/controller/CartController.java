package com.zerobase.everycampingbackend.cart.controller;

import com.zerobase.everycampingbackend.cart.domain.dto.CartProductDto;
import com.zerobase.everycampingbackend.cart.service.CartService;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> createCarts(@AuthenticationPrincipal Customer customer,
        @PathVariable Long productId, @RequestBody @Valid @Min(1) Integer quantity) {

        cartService.createCart(customer, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<CartProductDto>> getCartProductList(
        @AuthenticationPrincipal Customer customer, Pageable pageable) {

        return ResponseEntity.ok(cartService.getCartProductList(customer, pageable));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateQuantity(@AuthenticationPrincipal Customer customer,
        @PathVariable Long productId, @RequestBody @Valid @Min(1) Integer updateQuantity) {

        cartService.updateQuantity(customer, productId, updateQuantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteCartProduct(@AuthenticationPrincipal Customer customer,
        @PathVariable Long productId) {

        cartService.deleteCartProduct(customer, productId);
        return ResponseEntity.ok().build();
    }
}
