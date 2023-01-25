package com.zerobase.everycampingbackend.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zerobase.everycampingbackend.domain.order.dto.OrderByCustomerDto;
import com.zerobase.everycampingbackend.domain.order.entity.OrderProduct;
import com.zerobase.everycampingbackend.domain.order.entity.Orders;
import com.zerobase.everycampingbackend.domain.order.form.GetOrdersByCustomerForm;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm;
import com.zerobase.everycampingbackend.domain.order.form.OrderForm.OrderProductForm;
import com.zerobase.everycampingbackend.domain.order.repository.OrderProductRepository;
import com.zerobase.everycampingbackend.domain.order.repository.OrdersRepository;
import com.zerobase.everycampingbackend.domain.order.service.OrderService;
import com.zerobase.everycampingbackend.domain.order.type.OrderStatus;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.repository.ProductRepository;
import com.zerobase.everycampingbackend.domain.product.type.ProductCategory;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import com.zerobase.everycampingbackend.domain.user.repository.CustomerRepository;
import com.zerobase.everycampingbackend.domain.user.repository.SellerRepository;
import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
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

    @Autowired
    SellerRepository sellerRepository;

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
        Customer customer = createCustomer("ksj2083@naver.com");
        Long productId1 = createProduct("텐트1", 300, 5, ProductCategory.TENT);
        Long productId2 = createProduct("텐트2", 200, 5, ProductCategory.TENT);

        OrderProductForm form1 = OrderProductForm.builder()
            .productId(productId1)
            .quantity(5)
            .build();

        OrderProductForm form2 = OrderProductForm.builder()
            .productId(productId2)
            .quantity(4)
            .build();

        OrderForm orderForm = OrderForm.builder()
            .name("김세종")
            .address("대구시 남구")
            .phone("01086352083")
            .request("빠른 배송 바랍니다.")
            .orderProductFormList(List.of(form1, form2))
            .build();

        //when
        orderService.order(customer, orderForm);

        //then
        Orders orders = ordersRepository.findAll().get(0);
        assertEquals(customer.getId(), orders.getCustomer().getId());
        assertEquals("김세종", orders.getName());
        assertEquals("대구시 남구", orders.getAddress());
        assertEquals("01086352083", orders.getPhone());

        assertEquals("텐트1", orders.getRepresentProductName());
        assertEquals(1500+800, orders.getTotalAmount());

        OrderProduct orderProduct1 = orderProductRepository.findAll().get(0);
        OrderProduct orderProduct2 = orderProductRepository.findAll().get(1);
        Product product1 = productRepository.findById(productId1).orElseThrow();
        Product product2 = productRepository.findById(productId2).orElseThrow();

        assertEquals(productId1, orderProduct1.getProduct().getId());
        assertEquals(5, orderProduct1.getQuantity());
        assertEquals(300 * 5, orderProduct1.getAmount());
        assertEquals(orders.getId(), orderProduct1.getOrders().getId());
        assertEquals(product1.getStock(), 0);

        assertEquals(productId2, orderProduct2.getProduct().getId());
        assertEquals(4, orderProduct2.getQuantity());
        assertEquals(200 * 4, orderProduct2.getAmount());
        assertEquals(orders.getId(), orderProduct2.getOrders().getId());
        assertEquals(product2.getStock(), 1);
    }

    @Test
    @DisplayName("주문 실패 - 재고 부족으로 인한 실패")
    void createOrderAmountUnder1000() throws Exception {

        //given
        Customer customer = createCustomer("ksj2083@naver.com");
        Long productId1 = createProduct("텐트1", 300, 5, ProductCategory.TENT);
        Long productId2 = createProduct("텐트2", 200, 5, ProductCategory.TENT);

        OrderProductForm form1 = OrderProductForm.builder().productId(productId1)
            .quantity(5)
            .build();

        OrderProductForm form2 = OrderProductForm.builder().productId(productId2)
            .quantity(6)
            .build();

        OrderForm orderForm = OrderForm.builder()
            .name("김세종")
            .address("대구시 남구")
            .phone("01086352083")
            .request("빠른 배송 바랍니다.")
            .orderProductFormList(List.of(form1, form2))
            .build();

        //when
        CustomException ex = (CustomException) assertThrows(RuntimeException.class, () -> {
            orderService.order(customer, orderForm);
        });

        //then
        assertEquals(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK, ex.getErrorCode());

        Product product1 = productRepository.findById(productId1).orElseThrow();
        assertEquals(5, product1.getStock());

        assertTrue(orderProductRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("고객 주문목록 조회 성공")
    void getOrdersByCustomerSuccess() throws Exception {

        //given
        Customer customer = createCustomer("ksj2083@naver.com");
        Long productId1 = createProduct("텐트1", 300, 10, ProductCategory.TENT);
        Long productId2 = createProduct("텐트2", 200, 10, ProductCategory.TENT);

        OrderProductForm form1 = OrderProductForm.builder().productId(productId1)
            .quantity(5)
            .build();

        OrderProductForm form2 = OrderProductForm.builder().productId(productId2)
            .quantity(4)
            .build();

        OrderForm orderForm = OrderForm.builder()
            .name("김세종")
            .address("대구시 남구")
            .phone("01086352083")
            .request("빠른 배송 바랍니다.")
            .orderProductFormList(List.of(form1, form2))
            .build();

        orderService.order(customer, orderForm);

        PageRequest pageRequest = PageRequest.of(0, 5);
        GetOrdersByCustomerForm form = GetOrdersByCustomerForm.builder().build();

        //when
        Page<OrderByCustomerDto> result = orderService.getOrdersByCustomer(form,
            customer.getId(), pageRequest);

        //then
        OrderByCustomerDto dto = result.getContent().get(0);

        assertEquals(2, dto.getOrderProductCount());
        assertEquals(1500+800, dto.getTotalAmount());
    }

    @Test
    @DisplayName("구매확정 성공")
    void confirmSuccess() throws Exception {

        //given
        Customer customer = createCustomer("ksj2083@naver.com");

        Long productId1 = createProduct("텐트1", 300, 10, ProductCategory.TENT);

        OrderProductForm form1 = OrderProductForm.builder().productId(productId1)
            .quantity(5)
            .build();

        OrderForm orderForm = OrderForm.builder()
            .orderProductFormList(List.of(form1))
            .build();

        orderService.order(customer, orderForm);

        OrderProduct orderProduct = orderProductRepository.findAll().get(0);
        Long orderProductId = orderProduct.getId();

        //when
        orderService.confirm(customer, orderProductId);

        //then
        OrderProduct result = orderProductRepository.findAll().get(0);
        assertEquals(result.getId(), orderProductId);
        assertEquals(result.getStatus(), OrderStatus.CONFIRM);
        assertEquals(LocalDate.now(), result.getConfirmedAt().toLocalDate());
    }


    private Long createProduct(String name, int price, int stock, ProductCategory category) {
        Product product = Product.builder()
            .name(name)
            .category(category)
            .price(price)
            .stock(stock)
            .onSale(true)
            .seller(createSeller())
            .build();

        Product saved = productRepository.save(product);
        return saved.getId();
    }

    private Customer createCustomer(String email) {
        Customer customer = Customer.builder()
            .email(email)
            .nickName("고객닉네임")
            .build();

        return customerRepository.save(customer);
    }

    private Seller createSeller() {
        Seller seller = Seller.builder()
            .email("seller@naver.com")
            .nickName("판매자닉네임")
            .build();

        return sellerRepository.save(seller);
    }
}