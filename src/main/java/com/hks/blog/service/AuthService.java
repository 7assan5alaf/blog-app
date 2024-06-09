package com.hks.blog.service;

import com.hks.blog.dto.SignInDto;
import com.hks.blog.dto.SignUpDto;
import com.hks.blog.entity.User;
import com.hks.blog.entity.UserProfile;
import com.hks.blog.exception.EntityNotFound;
import com.hks.blog.exception.ErrorCodes;
import com.hks.blog.repository.UserProfileRepository;
import com.hks.blog.repository.UserRepository;
import com.hks.blog.response.MessageResponse;
import com.hks.blog.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public MessageResponse signUp(SignUpDto signUpDto) throws MessagingException {
        if (!signUpDto.getConfirmPassword().equals(signUpDto.getNewPassword()))
            return MessageResponse.builder()
                .message(ErrorCodes
                        .NEW_PASSWORD_DOES_NOT_MATCH.getDescription())
                .build();
        var user= User
                .builder()
                .email(signUpDto.getEmail())
                .phoneNumber(signUpDto.getPhoneNumber())
                .password(passwordEncoder.encode(signUpDto.getNewPassword()))
                .fullName(signUpDto.getFullName())
                .role("USER")
                .enable(false)
                .build();
        var profile= UserProfile.builder()
                .user(user)
                .createdBy(user.getEmail())
                .build();
        userRepository.save(user);
        userProfileRepository.save(profile);
        otpService.sendOtpToUser(user);
        return MessageResponse.builder()
                .message("Registration successfully.We send mail to you to activation your account")
                .build();
    }

    public MessageResponse activateAccount(String otp) throws MessagingException {
        var code=otpService.findByOtp(otp);
        if(code.getUser().isEnable())return MessageResponse.builder()
                .message("This Account is enabled")
                .build();
        if(LocalDateTime.now().isAfter(code.getExpiredAt())){
            otpService.sendOtpToUser(code.getUser());
            otpService.deleteByOtp(otp);
            return MessageResponse.builder()
                    .message("This otp is expired. we send another otp")
                    .build();
        }
        code.setValidatedAt(LocalDateTime.now());
        code.getUser().setEnable(true);
        userRepository.save(code.getUser());
        otpService.saveOtp(code);
        return MessageResponse.builder()
                .message("Activation account successfully")
                .build();
    }
   public MessageResponse signIn(SignInDto signInDto){
        var auth=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail()
                        ,signInDto.getPassword()));
        var user=(User)auth.getPrincipal();
       Map<String,Object>claims=new HashMap<>();
       claims.put("fullName",user.getFullName());
       claims.put("userId",user.getId());
       var token=jwtService.generateToken(user,claims);
       return MessageResponse.builder()
               .message(token)
               .build();
   }

   public String sendOtpToUser(String email) throws MessagingException {
        var user=userRepository.findByEmail(email)
                .orElseThrow(()->new EntityNotFound("the email not found"));
        otpService.sendOtpFromForgetPassword(user);
        return "Send otp to your email";
   }
   public String verifyOtp(String email,String code) throws MessagingException {
       var user=userRepository.findByEmail(email)
               .orElseThrow(()->new EntityNotFound("the email not found"));
       var otp=otpService.findByOtp(code);
       if (!otp.getUser().equals(user))return "invalid otp";
       if (LocalDateTime.now().isAfter(otp.getExpiredAt())){
           otpService.sendOtpFromForgetPassword(user);
           otpService.deleteByOtp(code);
           return "This otp is expired. we send another otp";
       }
       otp.setValidatedAt(LocalDateTime.now());
       otpService.saveOtp(otp);
       return "Otp verified!";
   }
   public MessageResponse forgetPassword(String email,String newPassword,String confirmPassword){
        if (!newPassword.equals(confirmPassword))
            return MessageResponse.builder()
                    .message(ErrorCodes.NEW_PASSWORD_DOES_NOT_MATCH.getDescription())
                    .build();
       var user=userRepository.findByEmail(email)
               .orElseThrow(()->new EntityNotFound("the email not found"));
       user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return MessageResponse.builder()
                .message("changed password successfully")
                .build();
   }

}
