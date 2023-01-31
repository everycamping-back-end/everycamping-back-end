package com.zerobase.everycampingbackend.domain.chat.repository;


import com.zerobase.everycampingbackend.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
