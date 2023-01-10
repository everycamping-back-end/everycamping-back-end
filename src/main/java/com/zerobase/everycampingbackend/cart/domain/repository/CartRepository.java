package com.zerobase.everycampingbackend.cart.domain.repository;

import com.zerobase.everycampingbackend.cart.domain.entity.CartProduct;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartProduct, Long> {

  @EntityGraph(attributePaths = {"product"}, type = EntityGraphType.LOAD)
  Page<CartProduct> findAllByCustomerId(Long customerId, Pageable pageable);

  Optional<CartProduct> findByCustomerIdAndProductId(Long customerId, Long productId);
}
