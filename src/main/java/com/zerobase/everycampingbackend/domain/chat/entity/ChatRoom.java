package com.zerobase.everycampingbackend.domain.chat.entity;

import com.zerobase.everycampingbackend.domain.chat.form.CreateChatRoomForm;
import com.zerobase.everycampingbackend.domain.chat.type.ChatRoomStatus;
import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requesterEmail;
    private UserType requesterUserType;
    private String requesteeEmail;
    private UserType requesteeUserType;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus;

    public static ChatRoom from(CreateChatRoomForm form) {
        return ChatRoom.builder()
                .requesterEmail(form.getRequesterEmail())
                .requesterUserType(form.getRequesterUserType())
                .requesteeEmail(form.getRequesteeEmail())
                .requesteeUserType(form.getRequesteeUserType())
                .chatRoomStatus(ChatRoomStatus.OPEN)
                .build();
    }
}
