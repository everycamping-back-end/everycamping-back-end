package com.zerobase.everycampingbackend.domain.chat.dto;

import com.zerobase.everycampingbackend.domain.chat.entity.Message;
import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageDto {

    private Long id;
    private Long chatRoomId;
    private String userEmail;
    private String userNickname;
    private UserType userType;
    private String content;
    private LocalDateTime createdAt;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoomId())
                .userEmail(message.getUserEmail())
                .userType(message.getUserType())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
