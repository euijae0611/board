package com.portfolio.board.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {
    // Thread-safe collection to store verification codes
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> expirationTimes = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService; // Assuming you have an EmailService that sends the email

    // Save the verification code and its expiration time
    public void sendAndSaveVerificationCode(String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        expirationTimes.put(email, LocalDateTime.now().plusMinutes(3)); // Code expires in 5 minutes
        emailService.sendVerificationEmail(email, code); // Send the email
    }

    // Verify the code
    public boolean verifyCode(String email, String code) {
        String validCode = verificationCodes.get(email);
        LocalDateTime expirationTime = expirationTimes.get(email);

        if (validCode != null && validCode.equals(code) && expirationTime != null && LocalDateTime.now().isBefore(expirationTime)) {
            verificationCodes.remove(email); // Code is correct and not expired, remove it
            expirationTimes.remove(email); // Remove expiration time as well
            return true;
        }
        return false;
    }

    // Call this method periodically to clean up expired verification codes
    public void cleanupExpiredVerificationCodes() {
        expirationTimes.entrySet().removeIf(entry -> LocalDateTime.now().isAfter(entry.getValue()));
    }

    // Generate a random 6-digit verification code
    private String generateVerificationCode() {
        int code = (int) (Math.random() * 900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }
}
