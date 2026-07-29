package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.EmailVerificationToken;
import com.kalchelin.kalchelin_road.entity.PasswordResetToken;
import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.exception.*;
import com.kalchelin.kalchelin_road.repository.EmailVerificationTokenRepository;
import com.kalchelin.kalchelin_road.repository.PasswordResetTokenRepository;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;      // @Bean으로 등록한 해싱 도구
    private final EmailVerificationTokenRepository tokenRepository;
    private final MailService mailService;
    private final PasswordResetTokenRepository resetTokenRepository;

    // 생성자 주입: Repository와 PasswordEncoder 둘 다 Spring이 넣어줌
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailVerificationTokenRepository tokenRepository, MailService mailService, PasswordResetTokenRepository resetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
        this.resetTokenRepository = resetTokenRepository;
    }

    // 회원가입: 아이디 중복 확인 -> 비밀번호 해싱 -> 저장
    @Transactional
    public User signup(String username, String password, String email) {
        // (1) 이미 있는 아이디면 거부
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
        }

        if (userRepository.existsByEmail((email))) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다");
        }

        // (2) 비밀번호를 해싱 (평문 -> 알아볼 수 없는 문자열)
        String encodedPassword = passwordEncoder.encode(password);

        // (3) User 객체 생성 (권한은 일단 USER로)
        User user = new User(username, encodedPassword, Role.USER, email);

        // (4) DB에 저장
        userRepository.save(user);

        // 인증 토큰 생성 후 메일 발송
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user, LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);

        mailService.sendVerificationMail(email, token);

        return user;
    }

    // 인증 처리 메서드
    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken found = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 인증 링크입니다."));
        if (found.isExpired()) {
            throw new InvalidTokenException("만료된 인증 링크입니다. 다시 요청해주세요");
        }

        User user = found.getUser();
        user.verifyEmail();
        tokenRepository.delete(found);      // 일회용 - 쓰고 나면 삭제
        // verifyEmail 맨 끝에 임시로
    }

    // 비밀번호 재설정 요청
    @Transactional
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            // 기존 토큰 정리 (재요청 대비)
            resetTokenRepository.deleteByUser(user);

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(30));
            resetTokenRepository.save(resetToken);
            mailService.sendPasswordResetMail(email, token);
        });
        // 이메일이 없어도 아무 일 없이 조용히 끝냄
    }

    // 비밀번호 재설정
    @Transactional
    public void confirmPasswordReset(String token, String newPassword) {
        PasswordResetToken found = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 링크입니다."));
        if (found.isExpired()) {
            throw new InvalidTokenException("만료된 링크입니다. 다시 요청해주세요");
        }

        User user = found.getUser();
        String encoded = passwordEncoder.encode(newPassword);   // 새 비번도 해싱
        user.changePassword(encoded);           // 엔티티 메서드로 변경
        resetTokenRepository.delete(found);     // 일회용
    }

    // 탈퇴
    @Transactional
    public void withdraw(User sessionUser, String rawPassword) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        // 비밀번호 재확인
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        user.withdraw();    // deleted = true, deletedAt 기록
    }
}
