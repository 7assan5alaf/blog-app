package com.hks.blog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    @NotBlank(message = "you must enter old password")
    private String oldPassword;
    @NotBlank(message = "you must enter new password")
    @Min(value = 8,message = "you must enter minimum 8 number")
    private String newPassword;
    @NotBlank(message = "you must enter new password")
    @Min(value = 8,message = "you must enter minimum 8 number")
    private String confirmPassword;
}

