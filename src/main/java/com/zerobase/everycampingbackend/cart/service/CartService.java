package com.zerobase.everycampingbackend.cart.service;

import com.zerobase.everycampingbackend.cart.domain.dto.CartProductDto;
import com.zerobase.everycampingbackend.cart.domain.entity.CartProduct;
import com.zerobase.everycampingbackend.cart.domain.form.CreateCartForm;
import com.zerobase.everycampingbackend.cart.domain.form.UpdateQuantityForm;
import com.zerobase.everycampingbackend.cart.domain.repository.CartRepository;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.service.ProductService;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CustomerService customerService;
  private final ProductService productService;
  private final CartRepository cartRepository;

  @Transactional
  public void createCart(CreateCartForm form, Long productId) {

    //로그인한 Customer 관련 로직 추가 예정

    Product product = productService.getProductById(productId);
    if (product.getStock() < form.getQuantity()) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
    }

    cartRepository.save(CartProduct.of(product, customerService.getCustomerById((form.getCustomerId())),
        form.getQuantity()));
  }

  /**
   * 재고 부족 시 주문가능한 최대 수량을 장바구니에 저장하고 response에 담아 준다.
   */
  @Transactional
  public Page<CartProductDto> getCartProductList(Long customerId, Pageable pageable) {

    //로그인한 Customer 관련 로직 추가 예정

    Page<CartProduct> cartProductList = cartRepository.findAllByCustomerId(customerId, pageable);

    return cartProductList.map(
        m -> m.getQuantity() > m.getProduct().getStock() ? handlingOutOfStock(m) :
            CartProductDto.of(m, false));
  }

  private CartProductDto handlingOutOfStock(CartProduct cartProduct) {
    cartProduct.setQuantity(cartProduct.getProduct().getStock());
    cartRepository.save(cartProduct);
    return CartProductDto.of(cartProduct, true);
  }

  @Transactional
  public void updateQuantity(Long productId, UpdateQuantityForm updateQuantityForm) {
    //로그인한 Customer 관련 로직 추가 예정

    CartProduct cartProduct = cartRepository.findByCustomerIdAndProductId(
            updateQuantityForm.getCustomerId(), productId)
        .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

    if (cartProduct.getProduct().getStock() < updateQuantityForm.getUpdateQuantity()) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
    }

    cartProduct.setQuantity(updateQuantityForm.getUpdateQuantity());
    cartRepository.save(cartProduct);
  }

}
