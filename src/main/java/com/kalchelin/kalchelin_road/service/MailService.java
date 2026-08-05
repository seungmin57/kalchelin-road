package com.kalchelin.kalchelin_road.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    // HTML 메일 한 통 발송
    public void send(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true = multipart 허용, "UTF-8" = 한글 인코딩
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("메일 발송에 실패했습니다.", e);
        }

    }

    // 인증 메일 발송
    public void sendVerificationMail(String to, String token) {
        String link = baseUrl + "/api/users/verify?token=" + token;
        String html = """
                <div style = "font-family:sans-serif;max-
                width:480px;margin:0 auto;padding:24px">
                <h2 style="color:#333">[칼슐랭로드] 회원가입을 환영합니다</h2>
                <p>아래 버튼을 눌러 이메일 인증을 완료해주세요.</p>
                <p style="margin:24px 0">
                    <a href="%s" style="background:#2d6a4f;color:#fff;padding:12px 24px;
                       text-decoration:none;border-radius:4px;display:inline-block">
                       이메일 인증하기
                    </a>
                </p>
                <p style="color:#888;font-size:13px">버튼이 안 눌리면 아래 주소를 복사해 붙여넣으세요.<br>
                    <span style="word-break:break-all">%s</span>
                </p>
                <p style="color:#888;font-size:13px">이 링크는 24시간 동안 유효합니다.</p>
                <div>
                """.formatted(link, link);
        send(to, "[칼슐랭로드] 이메일 인증", html);
    }


    // 비밀번호 재설정 메일 발송
    public void sendPasswordResetMail(String to, String token) {
        String link = baseUrl + "/api/users/reset-password?token=" + token;
        String text = """
                비밀번호 재설정 요청이 접수되었습니다.
                
                아래 링크에서 새 비밀번호를 설정해주세요.
                %s
                
                이 링크는 30분간 유효합니다.
                본인이 요청하지 않았다면 이 메일을 무시하세요.
                """.formatted(link);
        send(to, "[칼슐랭로드] 비밀번호 재설정", text);
    }

}
