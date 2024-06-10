package com.hks.blog.dto;

import lombok.*;

@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportDto {

    private String email;
    private String subject;
}
