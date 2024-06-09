package com.hks.blog.controller;

import com.hks.blog.dto.SignInDto;
import com.hks.blog.dto.SignUpDto;
import com.hks.blog.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?>signIn(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(authService.signIn(signInDto));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto) throws MessagingException {
        return ResponseEntity.ok(authService.signUp(signUpDto));
    }

    @PutMapping("/activate")
    public ResponseEntity<?> activationEmail(@RequestParam String otp) throws MessagingException {
        return ResponseEntity.ok(authService.activateAccount(otp));
    }
    @GetMapping("/get-otp")
    public ResponseEntity<?>sentOtpToUser(@RequestParam String email) throws MessagingException {
        return ResponseEntity.ok(authService.sendOtpToUser(email));
    }
    @GetMapping("/verify-otp/{email}")
    public ResponseEntity<?>verifyOtp(@PathVariable String email,@RequestParam String code) throws MessagingException {
        return ResponseEntity.ok(authService.verifyOtp(email,code));
    }
     @PutMapping("/forget-password/{email}")
    public ResponseEntity<?>forgetPassword(@PathVariable String email,@RequestParam String newPassword
            ,@RequestParam String confirmPassword ){
        return ResponseEntity.ok(authService.forgetPassword(email,newPassword,confirmPassword));
    }
}
