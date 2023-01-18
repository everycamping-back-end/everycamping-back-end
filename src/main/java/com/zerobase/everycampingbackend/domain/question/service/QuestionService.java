package com.zerobase.everycampingbackend.domain.question.service;

import com.zerobase.everycampingbackend.domain.question.entity.Message;
import com.zerobase.everycampingbackend.domain.question.form.MessageForm;
import com.zerobase.everycampingbackend.domain.question.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final MessageRepository messageRepository;

    public void addQuestionMessage(MessageForm messageForm) {
        messageRepository.save(Message.from(messageForm));
    }

//    public String generateQuestionRoom() {
//        while (true) {
//            String questionRoomId = UUID.randomUUID().toString();
//            if (messageRepository.existsByQuestionRoomId(questionRoomId)) continue;
//            return questionRoomId;
//        }
//    }
}
