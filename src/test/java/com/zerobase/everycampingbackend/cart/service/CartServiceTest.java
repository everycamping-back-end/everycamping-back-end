package com.zerobase.everycampingbackend.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerobase.everycampingbackend.cart.domain.entity.Cart;
import com.zerobase.everycampingbackend.cart.domain.form.CreateCartForm;
import com.zerobase.everycampingbackend.cart.domain.repository.CartRepository;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.repository.ProductRepository;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.domain.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
class CartServiceTest {

  @Autowired
  CartService cartService;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  CartRepository cartRepository;

  @Test
  @DisplayName("장바구니 넣기 실패 - 재고가 부족해서 실패")
  @Transactional
  void createCartProductNotEnoughStock() throws Exception {

    //given
    Long customerId = createCustomer("ksj2083@naver.com");
    CreateCartForm createCartForm = CreateCartForm.builder()
        .customerId(customerId)
        .count(6)
        .build();

    Long productId = createProduct("잡템", 5, ProductCategory.TENT);

    //when
    CustomException ex = (CustomException)assertThrows(RuntimeException.class, () -> {
      cartService.createCart(createCartForm, productId);
    });

    //then
    assertEquals(ex.getErrorCode(), ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
  }

  @Test
  @DisplayName("장바구니 넣기 성공")
  @Transactional
  void createCartSuccess() throws Exception {

    //given
    Long customerId = createCustomer("ksj2083@naver.com");
    CreateCartForm createCartForm = CreateCartForm.builder()
        .customerId(customerId)
        .count(5)
        .build();

    Long productId = createProduct("잡템", 5, ProductCategory.TENT);

    //when
    cartService.createCart(createCartForm, productId);

    //then
    PageRequest pageRequest = PageRequest.of(0, 5);
    Page<Cart> result = cartRepository.findAllByCustomerId(customerId, pageRequest);

    assertEquals(result.getContent().get(0).getCount(), 5);
    assertEquals(result.getContent().get(0).getProduct().getName(), "잡템");
  }


  private Long createProduct(String name, int stock, ProductCategory category) {
    Product product = Product.builder()
        .name(name)
        .category(category)
        .stock(stock)
        .build();

    Product saved = productRepository.save(product);
    return saved.getId();
  }

  private Long createCustomer(String email){
    Customer customer = Customer.builder()
        .email(email)
        .build();

    Customer saved = customerRepository.save(customer);
    return saved.getId();
  }

}