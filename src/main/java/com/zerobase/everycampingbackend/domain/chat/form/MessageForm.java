package com.zerobase.everycampingbackend.domain.chat.form;

import com.zerobase.everycampingbackend.domain.chat.type.UserType;
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

    private String chatRoomId;
    private String userEmail;
    private String userNickname;
    private UserType userType;
    private String content;
    private LocalDateTime createdAt;
}
