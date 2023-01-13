package com.zerobase.everycampingbackend.question.service;

import com.zerobase.everycampingbackend.question.domain.entity.Message;
import com.zerobase.everycampingbackend.question.domain.form.MessageForm;
import com.zerobase.everycampingbackend.question.domain.repository.MessageRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final MessageRepository messageRepository;

    public void addQuestionMessage(MessageForm form, Long customerId) {
        messageRepository.save(
            Message.builder()
                .customerId(customerId)
                .content(form.getContent())
                .createdAt(LocalDateTime.now())
                .build()
        );
    }
}
