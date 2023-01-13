package com.zerobase.everycampingbackend.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderForm;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderProductForm;
import com.zerobase.everycampingbackend.order.domain.model.Orders;
import com.zerobase.everycampingbackend.order.domain.repository.OrderProductRepository;
import com.zerobase.everycampingbackend.order.domain.repository.OrdersRepository;
import java.util.ArrayList;
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
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    public void clean() {
        ordersRepository.deleteAll();
        orderProductRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 성공")
    void createOrderSuccess() throws Exception {

        //given
        List<CreateOrderProductForm> orderProductList = new ArrayList<>();
        orderProductList.add(createOrderProductForm(0L, 3, 500));
        orderProductList.add(createOrderProductForm(3L, 2, 500));

        CreateOrderForm createOrderForm = new CreateOrderForm(orderProductList);

        //when
        Orders order = orderService.createOrder(createOrderForm);

        //then
        assertEquals(order.getAmount(), 1000);
    }

    @Test
    @DisplayName("주문 실패 - 총 금액이 1000원 미만이라서 실패")
    void createOrderAmountUnder1000() throws Exception {

        //given
        List<CreateOrderProductForm> orderProductList = new ArrayList<>();
        orderProductList.add(createOrderProductForm(0L, 3, 500));
        orderProductList.add(createOrderProductForm(3L, 2, 499));

        CreateOrderForm createOrderForm = new CreateOrderForm(orderProductList);

        //when
        CustomException ex = (CustomException) assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(createOrderForm);
        });

        //then
        assertEquals(ex.getErrorCode(), ErrorCode.ORDER_AMOUNT_UNDER_1000);
    }


    private CreateOrderProductForm createOrderProductForm(Long productId, Integer count,
        Integer partialAmount) {
        return CreateOrderProductForm.builder()
            .productId(productId)
            .count(count)
            .partialAmount(partialAmount)
            .build();
    }
}