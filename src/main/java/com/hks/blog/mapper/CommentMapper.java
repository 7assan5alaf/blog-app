package com.hks.blog.mapper;

import com.hks.blog.entity.Comment;
import com.hks.blog.response.CommentResponse;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {


    public CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .comment(comment.getSynopsis())
                .username(comment.getUser().getFullName())
                .build();
    }
}
