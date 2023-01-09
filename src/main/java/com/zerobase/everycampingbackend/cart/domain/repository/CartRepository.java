package com.zerobase.everycampingbackend.cart.domain.repository;

import com.zerobase.everycampingbackend.cart.domain.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Page<Cart> findAllByCustomerId(Long CustomerId, Pageable pageable);
}
