package com.zerobase.everycampingbackend.domain.user.repository;

import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByEmail(String email);

    boolean existsByEmail(String email);
}
