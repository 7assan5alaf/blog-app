package com.hks.blog.response;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private String username;
    private String report;
}
