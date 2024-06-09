package com.hks.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInDto {
    @Email(message = "you must enter valid email")
    private String email;
    @NotBlank(message = "you must enter password")
    private String password;
}
