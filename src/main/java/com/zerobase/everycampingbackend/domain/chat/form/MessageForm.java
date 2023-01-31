package com.zerobase.everycampingbackend.domain.chat.form;

import com.zerobase.everycampingbackend.domain.chat.type.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageForm {

    private Long chatRoomId;
    private String userEmail;
    private String userNickname;
    private UserType userType;
    private String content;
}
