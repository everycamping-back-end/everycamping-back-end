package com.zerobase.everycampingbackend.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.everycampingbackend.order.domain.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

}
