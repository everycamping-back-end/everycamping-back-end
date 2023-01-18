package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.question.form.MessageForm;
import com.zerobase.everycampingbackend.domain.question.service.QuestionService;
import java.time.LocalDateTime;
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

    @MessageMapping("/questions/{questionRoomId}")
    @SendTo("/topic/questions/{questionRoomId}")
    public MessageForm questionChat(@DestinationVariable String questionRoomId, MessageForm messageForm) {
        log.info("Question:: Message from " + questionRoomId + ", content : " + messageForm.getContent());
        messageForm.setQuestionRoomId(questionRoomId);
        messageForm.setCreatedAt(LocalDateTime.now());
        questionService.addQuestionMessage(messageForm);
        return messageForm;
    }

//    @PostMapping("/questions")
//    public String createQuestionRoom() {
//        String id = questionService.generateQuestionRoom();
//        log.info(id);
//        return id;
//    }
}
