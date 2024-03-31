package com.portfolio.board.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    // Send verification email with the generated code
    public void sendVerificationEmail(String toEmail, String code) {
        final String subject = "IT Portfolio Mall 회원가입 인증";
        final String content = "인증번호는 " + code + " 입니다.";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패", e);
            throw new RuntimeException("이메일 전송 실패: " + e.getMessage(), e);
        }
    }
}
