package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.dto.OrderDetailByCustomerDto;

public interface OrdersRepositoryCustom {

    OrderDetailByCustomerDto getOrderDetailByCustomer(Long orderId);
}
