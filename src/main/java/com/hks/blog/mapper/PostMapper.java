package com.hks.blog.mapper;

import com.hks.blog.entity.Post;
import com.hks.blog.response.PostResponse;
import com.hks.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostMapper {
    private final CommentMapper commentMapper;
    public PostResponse toPostResponse(Post post){
        var comments=post.getComments()
                .stream().map(commentMapper::toCommentResponse)
                .toList();
        return PostResponse
                .builder().synopsis(post.getSynopsis())
                .image(PostService.readFileFromLocation(post.getImage()))
                .likeCount(post.getLikeCount()).viewCount(post.getViewCount())
                .comments(comments)
                .owner(post.getUser().getFullName())
                .title(post.getTitle()).build();

    }
}
