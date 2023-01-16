package com.zerobase.everycampingbackend.question.controller;

import com.zerobase.everycampingbackend.question.domain.form.MessageForm;
import com.zerobase.everycampingbackend.question.service.QuestionService;
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

    private final QuestionService questionService;

    @MessageMapping("/question/{userId}")
    @SendTo("/topic/question/{userId}")
    public MessageForm questionChat(@DestinationVariable Long userId, MessageForm form) {
        log.info("Question:: Message from " + userId + ", content : " + form.getContent());
        questionService.addQuestionMessage(form, userId);
        return form;
    }
}
