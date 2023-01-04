package com.zerobase.everycampingbackend.user.domain.repository;

import com.zerobase.everycampingbackend.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
