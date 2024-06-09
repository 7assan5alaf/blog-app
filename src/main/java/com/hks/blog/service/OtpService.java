package com.hks.blog.service;

import com.hks.blog.entity.OtpCode;
import com.hks.blog.entity.User;
import com.hks.blog.exception.EntityNotFound;
import com.hks.blog.mail.EmailSender;
import com.hks.blog.repository.OtpRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailSender mailSender;
     public void sendOtpToUser(User user) throws MessagingException {
         var otp=generateAndSaveOtp(user);
         mailSender.sendEmail(user.getEmail(),"Thank you for registration in our site" +
                 ". we wish you view and read posts\nTo activation your account code is :"+ otp+"\nclick on the link :" +
                 " http://localhost:8081/blog/auth/activate","To activation account");
     }
     public void sendOtpFromForgetPassword(User user) throws MessagingException {
         var otp=generateAndSaveOtp(user);
         mailSender.sendEmail(user.getEmail(),"This is otp for you to forget password is : "+ otp+"\nclick on the link :" +
                 " http://localhost:8081/blog/auth/verfiy-otp/"+user.getEmail(),"Forget Password");
     }

    private String generateAndSaveOtp(User user) {
         var otp=generateOtp(6);
         var otpCode=OtpCode
                 .builder()
                 .otp(otp)
                 .user(user)
                 .createdAt(LocalDateTime.now())
                 .expiredAt(LocalDateTime.now().plusMinutes(15))
                 .build();
         otpRepository.save(otpCode);
         return otp;
    }


    private String generateOtp(int i) {
         final String digits="0123456789";
        SecureRandom random=new SecureRandom();
        StringBuilder builder=new StringBuilder();
        for (int k=0;k<i;k++){
            builder.append(random.nextInt(digits.length()));
        }
        return builder.toString();
    }

    public OtpCode findByOtp(String otp){
        return otpRepository.findByOtp(otp)
                .orElseThrow(()->new EntityNotFound("you must enter valid otp"));
    }

    public void saveOtp(OtpCode code) {
         otpRepository.save(code);
    }

    public void deleteByOtp(String otp) {
         var code=findByOtp(otp);
         otpRepository.delete(code);
    }
}
