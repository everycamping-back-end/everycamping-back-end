package com.zerobase.everycampingbackend.domain.chat.entity;

import com.zerobase.everycampingbackend.domain.chat.form.MessageForm;
import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatRoomId;
    private String userEmail;
    private String userNickname;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Message from(MessageForm messageForm) {
        return Message.builder()
            .chatRoomId(messageForm.getChatRoomId())
            .userEmail(messageForm.getUserEmail())
            .userNickname(messageForm.getUserNickname())
            .userType(messageForm.getUserType())
            .content(messageForm.getContent())
            .build();
    }
}
