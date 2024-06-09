package com.hks.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpCode {
    @Id
    @GeneratedValue
    private Long id;

    private String otp;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
