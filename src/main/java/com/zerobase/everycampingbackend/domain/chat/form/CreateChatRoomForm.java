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
public class CreateChatRoomForm {

    private String requesterEmail;
    private UserType requesterUserType;
    private String requesteeEmail;
    private UserType requesteeUserType;
}
