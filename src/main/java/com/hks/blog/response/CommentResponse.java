package com.hks.blog.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private String username;
    private String comment;
}
