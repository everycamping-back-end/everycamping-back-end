package com.zerobase.everycampingbackend.domain.chat.dto;

import com.zerobase.everycampingbackend.domain.chat.entity.ChatRoom;
import com.zerobase.everycampingbackend.domain.chat.type.ChatRoomStatus;
import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    private Long chatRoomId;
    private String requesterEmail;
    private UserType requesterUserType;
    private String requesteeEmail;
    private UserType requesteeUserType;
    private ChatRoomStatus chatRoomStatus;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getId())
                .requesterEmail(chatRoom.getRequesterEmail())
                .requesterUserType(chatRoom.getRequesterUserType())
                .requesteeEmail(chatRoom.getRequesteeEmail())
                .requesteeUserType(chatRoom.getRequesteeUserType())
                .chatRoomStatus(chatRoom.getChatRoomStatus())
                .build();
    }
}
