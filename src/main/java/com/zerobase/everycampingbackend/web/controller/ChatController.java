package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.chat.form.CreateChatRoomForm;
import com.zerobase.everycampingbackend.domain.chat.form.MessageForm;
import com.zerobase.everycampingbackend.domain.chat.service.ChatService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat-rooms/{roomId}")
    @SendTo("/topic/chat-rooms/{roomId}")
    public MessageForm chat(@DestinationVariable String roomId, MessageForm messageForm) {
        log.info("Question:: Message from " + roomId + ", content : " + messageForm);
        messageForm.setChatRoomId(roomId);
        messageForm.setCreatedAt(LocalDateTime.now());
        chatService.addChatMessage(messageForm);
        return messageForm;
    }

    @PostMapping("/chat-rooms")
    public ResponseEntity createChatRoom(@RequestBody CreateChatRoomForm createChatRoomForm) {
        log.info("Create Chat Room Request from: " + createChatRoomForm.getRequesterEmail());
        return ResponseEntity.ok(chatService.createChatRoom(createChatRoomForm));
    }
}
