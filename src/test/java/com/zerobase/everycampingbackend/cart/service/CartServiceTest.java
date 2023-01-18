package com.zerobase.everycampingbackend.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerobase.everycampingbackend.domain.cart.dto.CartProductDto;
import com.zerobase.everycampingbackend.domain.cart.entity.CartProduct;
import com.zerobase.everycampingbackend.domain.cart.repository.CartRepository;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import com.zerobase.everycampingbackend.domain.cart.service.CartService;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.repository.ProductRepository;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.domain.user.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
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

    @AfterEach
    public void clean() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("장바구니 넣기 실패 - 재고가 부족해서 실패")
    void createCartProductNotEnoughStock() throws Exception {

        //given
        Customer customer = createCustomer("ksj2083@naver.com");
        Long productId = createProduct("잡템", 5, ProductCategory.TENT);
        Integer quantity = 6;

        //when
        CustomException ex = (CustomException) assertThrows(RuntimeException.class, () -> {
            cartService.createCart(customer, productId, quantity);
        });

        //then
        assertEquals(ex.getErrorCode(), ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
    }

    @Test
    @DisplayName("장바구니 넣기 실패 - 이미 장바구니에 있는 상품이라 실패")
    void createCartProductAlreadyAdded() throws Exception {

        //given
        Customer customer = createCustomer("ksj2083@naver.com");
        Long productId = createProduct("잡템", 5, ProductCategory.TENT);

        addToCart(customer, productId, 2);

        Integer quantity = 2;

        //when
        CustomException ex = (CustomException) assertThrows(RuntimeException.class, () -> {
            cartService.createCart(customer, productId, quantity);
        });

        //then
        assertEquals(ex.getErrorCode(), ErrorCode.CART_PRODUCT_ALREADY_ADDED);
    }

    @Test
    @DisplayName("장바구니 넣기 성공")
    void createCartSuccess() throws Exception {

        //given
        Customer customer = createCustomer("ksj2083@naver.com");
        Long productId = createProduct("잡템", 5, ProductCategory.TENT);
        Integer quantity = 5;

        //when
        cartService.createCart(customer, productId, quantity);

        //then
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<CartProduct> result = cartRepository.findAllByCustomerId(customer.getId(), pageRequest);

        assertEquals(result.getContent().get(0).getQuantity(), 5);
        assertEquals(result.getContent().get(0).getProduct().getName(), "잡템");
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    void getCartProductSuccess() throws Exception {

        //given
        //유저 세팅
        Customer customer = createCustomer("ksj2083@naver.com");

        //상품 세팅
        Long productId1 = createProduct("텐트1", 5, ProductCategory.TENT);
        Long productId2 = createProduct("텐트2", 10, ProductCategory.TENT);

        //장바구니에 넣기
        addToCart(customer, productId1, 3);
        addToCart(customer, productId2, 5);

        //when

        //2번 상품은 재고가 1개 남게 되었음
        Product product = productService.getProductById(productId2);
        product.setStock(1);
        productRepository.save(product);

        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<CartProductDto> cartProductPage = cartService.getCartProductList(customer,
            pageRequest);

        //then
        CartProductDto dto1 = cartProductPage.getContent().get(0);
        CartProductDto dto2 = cartProductPage.getContent().get(1);

        assertEquals(productId1, dto1.getProductId());
        assertEquals(3, dto1.getQuantity());
        assertEquals(false, dto1.getIsQuantityChanged());

        assertEquals(productId2, dto2.getProductId());
        assertEquals(1, dto2.getQuantity());
        assertEquals(true, dto2.getIsQuantityChanged());

    }

    @Test
    @DisplayName("장바구니 수량 변경 성공")
    void updateQuantitySuccess() throws Exception {

        //given
        //유저 세팅
        Customer customer = createCustomer("ksj2083@naver.com");

        //상품 세팅
        Long productId = createProduct("텐트1", 5, ProductCategory.TENT);

        //장바구니에 넣기
        addToCart(customer, productId, 3);

        Integer updateQuantity = 4;

        //when
        cartService.updateQuantity(customer, productId, updateQuantity);

        //then
        CartProduct cartProduct = cartRepository.findByCustomerIdAndProductId(
            customer.getId(), productId).orElseThrow();

        assertEquals(4, cartProduct.getQuantity());
    }

    @Test
    @DisplayName("장바구니 수량 변경 실패 - 재고가 부족해서 실패")
    void updateQuantityNotEnoughStock() throws Exception {

        //given
        //유저 세팅
        Customer customer = createCustomer("ksj2083@naver.com");

        //상품 세팅
        Long productId = createProduct("텐트1", 5, ProductCategory.TENT);

        //장바구니에 넣기
        addToCart(customer, productId, 3);
        Integer updateQuantity = 6;

        //when
        CustomException ex = (CustomException) assertThrows(RuntimeException.class, () -> {
            cartService.updateQuantity(customer, productId, updateQuantity);
        });

        //then
        assertEquals(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK, ex.getErrorCode());
    }

    @Test
    @DisplayName("장바구니 물품 삭제 성공")
    void deleteCartProductSuccess() throws Exception {

        //given
        //유저 세팅
        Customer customer = createCustomer("ksj2083@naver.com");

        //상품 세팅
        Long productId = createProduct("텐트1", 5, ProductCategory.TENT);

        //장바구니에 넣기
        addToCart(customer, productId, 3);

        //when
        cartService.deleteCartProduct(customer, productId);

        //then
        Optional<CartProduct> optionalCartProduct = cartRepository.findByCustomerIdAndProductId(
            customer.getId(), productId);

        assertFalse(optionalCartProduct.isPresent());
    }

    private void addToCart(Customer customer, Long productId, Integer quantity) {
        cartService.createCart(customer, productId, quantity);
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

    private Customer createCustomer(String email) {
        Customer customer = Customer.builder()
            .email(email)
            .build();

        Customer saved = customerRepository.save(customer);
        return saved;
    }

}