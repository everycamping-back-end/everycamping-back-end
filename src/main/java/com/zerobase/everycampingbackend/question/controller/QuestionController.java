package com.zerobase.everycampingbackend.question.controller;

import com.zerobase.everycampingbackend.question.domain.form.MessageForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestionController {

    @MessageMapping("/question/{userId}")
    @SendTo("/topic/question/{userId}")
    public MessageForm send(@DestinationVariable String userId, MessageForm form) {
        log.info("Question from " + userId + ", content : " + form.getContent());
        return form;
    }
}
