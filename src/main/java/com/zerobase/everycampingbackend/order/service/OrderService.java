package com.zerobase.everycampingbackend.order.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderForm;
import com.zerobase.everycampingbackend.order.domain.form.CreateOrderProductForm;
import com.zerobase.everycampingbackend.order.domain.model.OrderProduct;
import com.zerobase.everycampingbackend.order.domain.model.Orders;
import com.zerobase.everycampingbackend.order.domain.repository.OrdersRepository;
import com.zerobase.everycampingbackend.order.type.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrdersRepository ordersRepository;

  // public Order createOrder(UserDetails userDetails, CreateOrderForm form) {
  // 	Long id = userDetails.getId();
  // 	//로직 진행
  // }

  @Transactional
  public Orders createOrder(CreateOrderForm form) {

    Orders orders = new Orders();
    //로그인한 Customer 관련 로직 추가 예정
    int totalAmount = 0;
    List<OrderProduct> orderProductList = new ArrayList<>();
    for (CreateOrderProductForm f : form.getOrderProducts()) {
      //product에 대한 예외처리 추가 예정(품절 등)
      int partialAmount = f.getPartialAmount();
      totalAmount += partialAmount;
      orderProductList.add(OrderProduct.builder()
          .orders(orders)
          .count(f.getCount())
          .partialAmount(partialAmount)
          .build());
    }

    if (totalAmount < 1000) {
      throw new CustomException(ErrorCode.ORDER_AMOUNT_UNDER_1000);
    }

    orders.setAmount(totalAmount);
    orders.setStatus(OrderStatus.DELIVERY_COMPLETE);
    orders.setOrderProductList(orderProductList);

    return ordersRepository.save(orders);
  }
}
