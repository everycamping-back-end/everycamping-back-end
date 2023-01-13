package com.zerobase.everycampingbackend.order.domain.repository;

import com.zerobase.everycampingbackend.order.domain.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
