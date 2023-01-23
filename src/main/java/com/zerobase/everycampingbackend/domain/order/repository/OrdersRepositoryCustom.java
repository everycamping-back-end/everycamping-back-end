package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.dto.OrderDetailByCustomerDto;
import java.util.List;

public interface OrdersRepositoryCustom {

    List<OrderDetailByCustomerDto> getOrderDetailByCustomer(Long orderId);
}
