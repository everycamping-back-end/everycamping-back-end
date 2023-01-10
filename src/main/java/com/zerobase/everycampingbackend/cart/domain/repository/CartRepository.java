package com.zerobase.everycampingbackend.cart.domain.repository;

import com.zerobase.everycampingbackend.cart.domain.entity.CartProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartProduct, Long> {
  Page<CartProduct> findAllByCustomerId(Long CustomerId, Pageable pageable);
}
