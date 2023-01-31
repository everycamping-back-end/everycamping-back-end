package com.zerobase.everycampingbackend.domain.chat.repository;


import com.zerobase.everycampingbackend.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatRoomIdOrderByIdAsc(Long chatRoomId);
}
