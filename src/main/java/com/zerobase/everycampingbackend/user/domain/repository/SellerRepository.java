package com.zerobase.everycampingbackend.user.domain.repository;

import com.zerobase.everycampingbackend.user.domain.entity.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
  Optional<Seller> findByEmail(String email);
}
