package com.zerobase.everycampingbackend.cart.service;

import com.zerobase.everycampingbackend.cart.domain.entity.Cart;
import com.zerobase.everycampingbackend.cart.domain.form.CreateCartForm;
import com.zerobase.everycampingbackend.cart.domain.repository.CartRepository;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.service.ProductService;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CustomerService customerService;
  private final ProductService productService;
  private final CartRepository cartRepository;

  public void createCart(CreateCartForm form, Long productId) {

    //로그인한 Customer 관련 로직 추가 예정

    Product product = productService.getProductById(productId);
    if (product.getStock() < form.getCount()) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
    }

    cartRepository.save(Cart.of(productService.getProductById(form.getCustomerId()),
        customerService.getCustomerById((form.getCustomerId())),
        form.getCount()));
  }
}
