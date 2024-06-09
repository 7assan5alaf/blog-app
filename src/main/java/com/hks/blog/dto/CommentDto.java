package com.hks.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @NotBlank(message = "you must add comment")
    private String comment;
    @NotBlank(message = "you must enter id from post")
    private Long postId;
}
