package com.zerobase.everycampingbackend.user.domain.repository;

import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByEmail(String email);


}
