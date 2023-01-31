package com.zerobase.everycampingbackend.domain.chat.service;

import com.zerobase.everycampingbackend.domain.chat.dto.ChatRoomDto;
import com.zerobase.everycampingbackend.domain.chat.entity.ChatRoom;
import com.zerobase.everycampingbackend.domain.chat.entity.Message;
import com.zerobase.everycampingbackend.domain.chat.form.CreateChatRoomForm;
import com.zerobase.everycampingbackend.domain.chat.form.MessageForm;
import com.zerobase.everycampingbackend.domain.chat.repository.ChatRoomRepository;
import com.zerobase.everycampingbackend.domain.chat.repository.MessageRepository;
import com.zerobase.everycampingbackend.domain.chat.type.ChatRoomStatus;
import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public void addChatMessage(MessageForm messageForm) {
        messageRepository.save(Message.from(messageForm));
    }

    public ChatRoomDto createChatRoom(CreateChatRoomForm form) {
        if (UserType.ADMIN.equals(form.getRequesteeEmail())) {
            form.setRequesteeEmail(getAdminEmail());
        }

        boolean previousRoomExists = chatRoomRepository.existsByRequesterEmailAndRequesteeEmailAndChatRoomStatus(
                form.getRequesterEmail(), form.getRequesteeEmail(), ChatRoomStatus.OPEN);

        if (previousRoomExists) {
            return ChatRoomDto.from(
                    chatRoomRepository.findByRequesterEmailAndRequesteeEmailAndChatRoomStatus(
                            form.getRequesterEmail(), form.getRequesteeEmail(), ChatRoomStatus.OPEN));
        }

        return ChatRoomDto.from(chatRoomRepository.save(ChatRoom.from(form)));
    }

    private String getAdminEmail() {
        return "admin";
    }
}
