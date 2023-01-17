package com.zerobase.everycampingbackend.question.domain.entity;

import com.zerobase.everycampingbackend.question.domain.form.MessageForm;
import com.zerobase.everycampingbackend.question.type.UserType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionRoomId;
    private String userEmail;
    private String userNickname;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String content;
    private LocalDateTime createdAt;

    public static Message from(MessageForm messageForm) {
        return Message.builder()
            .questionRoomId(messageForm.getQuestionRoomId())
            .userEmail(messageForm.getUserEmail())
            .userNickname(messageForm.getUserNickname())
            .userType(messageForm.getUserType())
            .content(messageForm.getContent())
            .build();
    }
}
