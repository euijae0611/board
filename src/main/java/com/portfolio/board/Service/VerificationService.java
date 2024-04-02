package com.portfolio.board.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> expirationTimes = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;

    public void sendAndSaveVerificationCode(String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        expirationTimes.put(email, LocalDateTime.now().plusMinutes(3));
        emailService.sendVerificationEmail(email, code);
    }

    public boolean verifyCode(String email, String code) {
        String validCode = verificationCodes.get(email);
        LocalDateTime expirationTime = expirationTimes.get(email);

        if (validCode != null && validCode.equals(code) && expirationTime != null && LocalDateTime.now().isBefore(expirationTime)) {
            verificationCodes.remove(email);
            expirationTimes.remove(email);
            return true;
        }
        return false;
    }

    public void cleanupExpiredVerificationCodes() {
        expirationTimes.entrySet().removeIf(entry -> LocalDateTime.now().isAfter(entry.getValue()));
    }

    private String generateVerificationCode() {
        int code = (int) (Math.random() * 900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }
}
