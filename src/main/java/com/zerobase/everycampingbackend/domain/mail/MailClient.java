package com.zerobase.everycampingbackend.domain.mail;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailClient {
    private final JavaMailSender javaMailSender;

    public void sendMail(MailForm form) throws MailException {
        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(form.getTo());
                mimeMessageHelper.setSubject(form.getSubject());
                mimeMessageHelper.setText(form.getText(), true);
            }
        };

        javaMailSender.send(msg);
    }
}
