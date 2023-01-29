package com.zerobase.everycampingbackend.domain.question.repository;


import com.zerobase.everycampingbackend.domain.question.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    boolean existsByQuestionRoomId(String questionRoomId);
}
