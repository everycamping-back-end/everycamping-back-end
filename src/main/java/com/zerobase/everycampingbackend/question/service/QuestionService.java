package com.zerobase.everycampingbackend.question.service;

import com.zerobase.everycampingbackend.question.domain.entity.Message;
import com.zerobase.everycampingbackend.question.domain.form.MessageForm;
import com.zerobase.everycampingbackend.question.domain.repository.MessageRepository;
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
