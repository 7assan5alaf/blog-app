package com.hks.blog.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

     public void sendEmail(String to,String body,String sub) throws MessagingException {

         MimeMessage message=mailSender.createMimeMessage();
         MimeMessageHelper helper=new MimeMessageHelper(message,true);

         helper.setTo(to);
         helper.setFrom("artgallery623@gmail.com");
         helper.setSubject(sub);
         helper.setText(body);
         mailSender.send(message);

     }
}
