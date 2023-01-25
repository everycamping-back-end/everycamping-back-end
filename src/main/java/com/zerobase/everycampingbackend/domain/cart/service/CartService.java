package com.zerobase.everycampingbackend.domain.cart.service;

import com.zerobase.everycampingbackend.domain.cart.dto.CartProductDto;
import com.zerobase.everycampingbackend.domain.cart.entity.CartProduct;
import com.zerobase.everycampingbackend.domain.cart.repository.CartRepository;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductService productService;
    private final CartRepository cartRepository;

    @Transactional
    public void addToCart(Customer customer, Long productId, Integer quantity) {

        Optional<CartProduct> cartProduct = cartRepository.findByCustomerIdAndProductId(
            customer.getId(), productId);
        if (cartProduct.isPresent()) {
            throw new CustomException(ErrorCode.CART_PRODUCT_ALREADY_ADDED);
        }

        Product product = productService.getProductById(productId);
        if (product.getStock() < quantity) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        }

        cartRepository.save(
            CartProduct.of(product, customer, quantity)
        );
    }

    /**
     * 재고 부족 시 주문가능한 최대 수량을 장바구니에 저장하고 response에 담아 준다.
     */
    @Transactional
    public Page<CartProductDto> getCartProductList(Customer customer, Pageable pageable) {

        Page<CartProduct> cartProductList = cartRepository.findAllByCustomerId(customer.getId(),
            pageable);

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
    public void updateQuantity(Customer customer, Long productId, Integer updateQuantity) {

        CartProduct cartProduct = cartRepository.findByCustomerIdAndProductId(
                customer.getId(), productId)
            .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        if (cartProduct.getProduct().getStock() < updateQuantity) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        }
        cartProduct.setQuantity(updateQuantity);
    }

    public void deleteCartProduct(Customer customer, Long productId) {

        CartProduct cartProduct = cartRepository.findByCustomerIdAndProductId(customer.getId(),
                productId)
            .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        cartRepository.delete(cartProduct);
    }

    public void deleteCartProductsIfExist(Customer customer, List<Long> productIdList) {

        List<CartProduct> cartProductList = cartRepository.findAllByCustomerId(customer.getId());

        List<CartProduct> delTarget = cartProductList.stream()
            .filter(e -> productIdList.contains(e.getProduct().getId()))
            .collect(Collectors.toList());

        if (delTarget.isEmpty()) {
            return;
        }

        cartRepository.deleteAllInBatch(delTarget);
    }
}
