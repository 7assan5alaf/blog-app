package com.hks.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank(message = "you must enter fullName")
    private String fullName;
    @NotBlank(message = "you must enter email")
    @Email(message = "you must enter valid email")
    private String email;
    @NotBlank(message = "you must enter password")
    private String newPassword;
    @NotBlank(message = "you must enter confirm password")
    private String confirmPassword;
    @NotBlank(message = "you must enter phone number")
    @Min(value = 11,message = "you must enter max number 11")
    private String phoneNumber;
}
