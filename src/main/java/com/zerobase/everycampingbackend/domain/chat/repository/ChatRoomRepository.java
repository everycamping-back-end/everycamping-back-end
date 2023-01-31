package com.zerobase.everycampingbackend.domain.chat.repository;

import com.zerobase.everycampingbackend.domain.chat.entity.ChatRoom;
import com.zerobase.everycampingbackend.domain.chat.type.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsByRequesterEmailAndRequesteeEmailAndChatRoomStatus(String requesterEmail, String requesteeEmail, ChatRoomStatus chatRoomStatus);
    ChatRoom findByRequesterEmailAndRequesteeEmailAndChatRoomStatus(String requesterEmail, String requesteeEmail, ChatRoomStatus chatRoomStatus);
    List<ChatRoom> findAllByRequesterEmailAndChatRoomStatus(String requesterEmail, ChatRoomStatus chatRoomStatus);
    List<ChatRoom> findAllByRequesteeEmailAndChatRoomStatus(String requesteeEmail, ChatRoomStatus chatRoomStatus);
}
