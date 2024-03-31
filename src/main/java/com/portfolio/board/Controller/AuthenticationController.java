package com.portfolio.board.Controller;

import com.portfolio.board.Config.VerificationRequest;
import com.portfolio.board.Service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthenticationController {

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest request) {
        boolean isCodeValid = verificationService.verifyCode(request.getEmail(), request.getCode());
        if (isCodeValid) {
            return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 인증 코드"));
        }
    }
}