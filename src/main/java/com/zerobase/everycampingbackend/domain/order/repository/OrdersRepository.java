package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.entity.Orders;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryCustom {

    Page<Orders> findAllByCustomerIdAndCreatedAtBetween(Long customerId, Pageable pageable,
        LocalDateTime start, LocalDateTime end);

    Page<Orders> findAllByCustomerId(Long customerId, Pageable pageable);
}
