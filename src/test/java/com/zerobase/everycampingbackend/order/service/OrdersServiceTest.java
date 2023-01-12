package com.zerobase.everycampingbackend.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.order.domain.entity.OrderProduct;
import com.zerobase.everycampingbackend.order.domain.entity.Orders;
import com.zerobase.everycampingbackend.order.domain.form.OrderForm;
import com.zerobase.everycampingbackend.order.domain.form.OrderForm.OrderProductForm;
import com.zerobase.everycampingbackend.order.domain.repository.OrderProductRepository;
import com.zerobase.everycampingbackend.order.domain.repository.OrdersRepository;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.domain.repository.ProductRepository;
import com.zerobase.everycampingbackend.product.type.ProductCategory;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.domain.repository.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class OrdersServiceTest {

  @Autowired
  OrderService orderService;

  @Autowired
  OrdersRepository ordersRepository;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  CustomerRepository customerRepository;

  @AfterEach
  void clean() {
    orderProductRepository.deleteAll();
    ordersRepository.deleteAll();
    customerRepository.deleteAll();
    productRepository.deleteAll();
  }

  @Test
  @DisplayName("주문 성공")
  void orderSuccess() throws Exception {

    //given
    Long customerId = createCustomer("ksj2083@naver.com");
    Long productId1 = createProduct("텐트1", 300,5, ProductCategory.TENT);
    Long productId2 = createProduct("텐트2", 200,5, ProductCategory.TENT);

    OrderProductForm form1 = OrderProductForm.builder().productId(productId1)
        .quantity(5)
        .build();

    OrderProductForm form2 = OrderProductForm.builder().productId(productId2)
        .quantity(4)
        .build();

    OrderForm orderForm = OrderForm.builder()
        .customerId(customerId)
        .orderProductFormList(List.of(form1, form2))
        .build();

    //when
    orderService.order(orderForm);

    //then
    Orders orders = ordersRepository.findAll().get(0);
    assertEquals(customerId, orders.getCustomer().getId());

    OrderProduct orderProduct1 = orderProductRepository.findAll().get(0);
    OrderProduct orderProduct2 = orderProductRepository.findAll().get(1);
    Product product1 = productRepository.findById(productId1).orElseThrow();
    Product product2 = productRepository.findById(productId2).orElseThrow();

    assertEquals(productId1, orderProduct1.getProduct().getId());
    assertEquals(5, orderProduct1.getQuantity());
    assertEquals(300*5, orderProduct1.getAmount());
    assertEquals(orders.getId(), orderProduct1.getOrders().getId());
    assertEquals(product1.getStock(), 0);

    assertEquals(productId2, orderProduct2.getProduct().getId());
    assertEquals(4, orderProduct2.getQuantity());
    assertEquals(200*4, orderProduct2.getAmount());
    assertEquals(orders.getId(), orderProduct2.getOrders().getId());
    assertEquals(product2.getStock(), 1);
  }

  @Test
  @DisplayName("주문 실패 - 재고 부족으로 인한 실패")
  void createOrderAmountUnder1000() throws Exception {

    //given
    Long customerId = createCustomer("ksj2083@naver.com");
    Long productId1 = createProduct("텐트1", 300,5, ProductCategory.TENT);
    Long productId2 = createProduct("텐트2", 200,5, ProductCategory.TENT);

    OrderProductForm form1 = OrderProductForm.builder().productId(productId1)
        .quantity(5)
        .build();

    OrderProductForm form2 = OrderProductForm.builder().productId(productId2)
        .quantity(6)
        .build();

    OrderForm orderForm = OrderForm.builder()
        .customerId(customerId)
        .orderProductFormList(List.of(form1, form2))
        .build();

    //when
    CustomException ex = (CustomException)assertThrows(RuntimeException.class, () -> {
      orderService.order(orderForm);
    });

    //then
    assertEquals(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK, ex.getErrorCode());

    Product product1 = productRepository.findById(productId1).orElseThrow();
    assertEquals(5, product1.getStock());

    assertTrue(orderProductRepository.findAll().isEmpty());
  }

  private Long createProduct(String name, int price, int stock, ProductCategory category) {
    Product product = Product.builder()
        .name(name)
        .category(category)
        .price(price)
        .stock(stock)
        .onSale(true)
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