package com.hks.blog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hks.blog.entity.Comment;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponse {
    private String title;
    private String synopsis;
    private String owner;
    private byte[] image;
    private int likeCount;
    private int viewCount;
    private List<CommentResponse> comments;
}
