package com.zerobase.everycampingbackend.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerobase.everycampingbackend.cart.domain.dto.CartProductDto;
import com.zerobase.everycampingbackend.cart.domain.entity.CartProduct;
import com.zerobase.everycampingbackend.cart.domain.form.CreateCartForm;
import com.zerobase.everycampingbackend.cart.domain.repository.CartRepository;
import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.repository.ProductRepository;
import com.zerobase.everycampingbackend.product.service.ProductService;
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

  @Autowired
  ProductService productService;

  @Test
  @DisplayName("장바구니 넣기 실패 - 재고가 부족해서 실패")
  @Transactional
  void createCartProductNotEnoughStock() throws Exception {

    //given
    Long customerId = createCustomer("ksj2083@naver.com");
    CreateCartForm createCartForm = CreateCartForm.builder()
        .customerId(customerId)
        .quantity(6)
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
        .quantity(5)
        .build();

    Long productId = createProduct("잡템", 5, ProductCategory.TENT);

    //when
    cartService.createCart(createCartForm, productId);

    //then
    PageRequest pageRequest = PageRequest.of(0, 5);
    Page<CartProduct> result = cartRepository.findAllByCustomerId(customerId, pageRequest);

    assertEquals(result.getContent().get(0).getQuantity(), 5);
    assertEquals(result.getContent().get(0).getProduct().getName(), "잡템");
  }

  @Test
  @DisplayName("장바구니 조회 성공")
  @Transactional
  void getCartProductSuccess() throws Exception {

    //given
    //유저 세팅
    Long customerId = createCustomer("ksj2083@naver.com");

    //상품 세팅
    Long productId1 = createProduct("텐트1", 5, ProductCategory.TENT);
    Long productId2 = createProduct("텐트2", 10, ProductCategory.TENT);

    //장바구니에 넣기
    addToCart(customerId, productId1, 3);
    addToCart(customerId, productId2, 5);

    //when

    //2번 상품은 재고가 1개 남게 되었음
    Product product = productService.getProductById(productId2);
    product.setStock(1);
    productRepository.save(product);

    PageRequest pageRequest = PageRequest.of(0, 5);
    Page<CartProductDto> cartProductPage = cartService.getCartProductList(customerId, pageRequest);

    //then
    CartProductDto dto1 = cartProductPage.getContent().get(0);
    CartProductDto dto2 = cartProductPage.getContent().get(1);

    assertEquals(productId1, dto1.getProductId());
    assertEquals(3, dto1.getQuantity());
    assertEquals(false, dto1.getIsCountChanged());

    assertEquals(productId2, dto2.getProductId());
    assertEquals(1, dto2.getQuantity());
    assertEquals(true, dto2.getIsCountChanged());

  }

  private void addToCart(Long customerId, Long productId, Integer quantity) {
    cartService.createCart(CreateCartForm.builder()
        .customerId(customerId)
        .quantity(quantity)
        .build(), productId);
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