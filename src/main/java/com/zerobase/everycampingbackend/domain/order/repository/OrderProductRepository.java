package com.zerobase.everycampingbackend.domain.order.repository;

import com.zerobase.everycampingbackend.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>,
    OrderProductRepositoryCustom {

}
