package com.zerobase.everycampingbackend.question.domain.repository;


import com.zerobase.everycampingbackend.question.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
