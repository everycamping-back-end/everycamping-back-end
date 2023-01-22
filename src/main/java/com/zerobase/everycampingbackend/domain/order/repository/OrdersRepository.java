package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findAllByCustomerId(Long customerId, Pageable pageable);
}
