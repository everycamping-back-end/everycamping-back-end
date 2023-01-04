package com.zerobase.everycampingbackend.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderForm;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderProductForm;
import com.zerobase.everycampingbackend.order.domain.model.Order;
import com.zerobase.everycampingbackend.order.domain.model.OrderProduct;
import com.zerobase.everycampingbackend.order.domain.repository.OrderRepository;
import com.zerobase.everycampingbackend.order.type.OrderStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  // public Order createOrder(UserDetails userDetails, CreateOrderForm form) {
  // 	Long id = userDetails.getId();
  // 	//로직 진행
  // }

  //form을 바로 내려받는게 맞는가?
  public Order createOrder(CreateOrderForm form) {
    //로그인한 Customer 관련 로직 추가 예정
    int totalAmount = 0;
    List<OrderProduct> orderProductList = new ArrayList<>();
    for (CreateOrderProductForm f : form.getOrderProducts()) {
      //product에 대한 예외처리 추가 예정(품절 등)
      int partialAmount = f.getPartialAmount();
      totalAmount += partialAmount;
      orderProductList.add(OrderProduct.builder()
          .id(f.getProductId())
          .count(f.getCount())
          .partialAmount(partialAmount)
          .build());
    }

    if (totalAmount < 1000) {
      throw new CustomException(ErrorCode.ORDER_AMOUNT_UNDER_1000);
    }

    return orderRepository.save(Order.builder()
        .amount(totalAmount)
        .status(OrderStatus.DELIVERY_COMPLETE)
        .orderProductList(orderProductList)
        .build());
  }
}
