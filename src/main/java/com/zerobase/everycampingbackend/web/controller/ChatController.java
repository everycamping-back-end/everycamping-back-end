package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.chat.dto.MessageDto;
import com.zerobase.everycampingbackend.domain.chat.form.CreateChatRoomForm;
import com.zerobase.everycampingbackend.domain.chat.form.MessageForm;
import com.zerobase.everycampingbackend.domain.chat.service.ChatService;
import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat-rooms/{chatRoomId}")
    @SendTo("/topic/chat-rooms/{chatRoomId}")
    public MessageDto chat(@DestinationVariable Long chatRoomId, MessageForm messageForm) {
        log.info("Question:: Message from " + chatRoomId + ", content : " + messageForm);
        return chatService.addChatMessage(messageForm, chatRoomId);
    }

    @PostMapping("/chat-rooms")
    public ResponseEntity<?> createChatRoom(@RequestBody CreateChatRoomForm createChatRoomForm) {
        log.info("Create Chat Room Request from: " + createChatRoomForm.getRequesterEmail());
        return ResponseEntity.ok(chatService.createChatRoom(createChatRoomForm));
    }

    @DeleteMapping("/chat-rooms/{chatRoomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long chatRoomId) {
        log.info("Delete Chat Room Id: " + chatRoomId);
        chatService.removeChatRoom(chatRoomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chat-rooms")
    public ResponseEntity<?> getChatRooms(@RequestParam String userEmail, @RequestParam UserType userType) {
        return ResponseEntity.ok(chatService.getChatRooms(userEmail, userType));
    }

    @GetMapping("/chat-rooms/{chatRoomId}/messages")
    public ResponseEntity<?> getPreviousChatMessages(@PathVariable Long chatRoomId) {
        log.info("Get Previous Message in Chatroom : " + chatRoomId);
        return ResponseEntity.ok(chatService.getPreviousMessages(chatRoomId));
    }
}
