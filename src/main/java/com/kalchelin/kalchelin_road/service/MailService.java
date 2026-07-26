package com.kalchelin.kalchelin_road.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    private final JavaMailSender mailSender;    // 스프링이 만들어주는 발송 도구

    // 보내는 사람 주소 (application-local.properties의 username을 재사용)
    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.base-url}")
    private String baseUrl;     // 링크에 쓸 서버 주소

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 텍스트 메일 한 통 발송
    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 인증 메일 발송
    public void sendVerificationMail(String to, String token) {
        String link = baseUrl + "/api/users/verify?token=" + token;
        String text = """
                칼슐랭로드 회원가입을 환영합니다.
                
                아래 링크를 눌러 이메일 인증을 완료해주세요.
                %s
                
                이 링크는 24시간 동안 유효합니다.
                """.formatted(link);
        send(to, "[칼슐랭로드] 이메일 인증", text);
    }


}
