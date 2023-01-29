package com.zerobase.everycampingbackend.domain.question.form;

import com.zerobase.everycampingbackend.domain.question.type.UserType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageForm {
    private String questionRoomId;
    private String userEmail;
    private String userNickname;
    private UserType userType;
    private String content;
    private LocalDateTime createdAt;
}
