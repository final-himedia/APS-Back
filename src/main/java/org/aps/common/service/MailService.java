package org.aps.common.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendTemporalPasswordMessage(String email, String temporalPassword) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(email);
            helper.setSubject("임시 비밀번호 안내");
            String content = "<p>안녕하세요.</p>"
                    + "<p>임시 비밀번호는 <strong>" + temporalPassword + "</strong> 입니다.</p>";
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("임시 비밀번호 이메일 전송 실패", e);
        }
    }
}
