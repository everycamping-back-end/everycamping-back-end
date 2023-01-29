package com.zerobase.everycampingbackend.domain.admin.repository;

import com.zerobase.everycampingbackend.domain.admin.entity.SellerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRequestRepository extends JpaRepository<SellerRequest, Long> {

}
