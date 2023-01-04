package com.zerobase.everycampingbackend.order.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.everycampingbackend.order.domain.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
