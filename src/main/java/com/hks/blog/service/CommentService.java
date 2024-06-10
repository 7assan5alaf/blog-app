package com.hks.blog.service;

import com.hks.blog.dto.CommentDto;
import com.hks.blog.entity.Comment;
import com.hks.blog.entity.User;
import com.hks.blog.exception.EntityNotFound;
import com.hks.blog.exception.OperationPermittedException;
import com.hks.blog.mapper.CommentMapper;
import com.hks.blog.repository.CommentRepository;
import com.hks.blog.repository.PostRepository;
import com.hks.blog.response.CommentResponse;
import com.hks.blog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public PostResponse addComment(CommentDto commentDto, Authentication auth){
        var post=postRepository.findById(commentDto.getPostId()).
                orElseThrow(()->new EntityNotFound("Post not found"));
           var user=(User)auth.getPrincipal();
        var comment= Comment.builder()
                .post(post)
                .user(user)
                .synopsis(commentDto.getComment())
                .build();
        commentRepository.save(comment);
        var comments=post.getComments().stream().map(commentMapper::toCommentResponse).toList();
        return PostResponse.builder()
                .title(post.getTitle())
                .comments(comments)
                .image(PostService.readFileFromLocation(post.getImage()))
                .owner(post.getUser().getFullName())
                .likeCount(post.getLikeCount())
                .synopsis(post.getSynopsis())
                .viewCount(post.getViewCount())
                .build();
    }


    public List<CommentResponse> viewAllCommentByPost(Long postId){
        var post=postRepository.findById(postId).
                orElseThrow(()->new EntityNotFound("Post not found"));
        var comments=commentRepository.findAllByPostId(post.getId());
        List<CommentResponse>commentResponses=comments
                .stream().map(commentMapper::toCommentResponse)
                .toList();
        return commentResponses;
    }

    public CommentResponse updateComment(Long commentId,String synopsis, Authentication auth){
        var user=(User)auth.getPrincipal();
        var comment=commentRepository.findById(commentId)
                .orElseThrow(()->new EntityNotFound("Comment not found"));
        if (!comment.getUser().getEmail().equals(user.getEmail())){
                throw  new OperationPermittedException("can not update this comment");
        }
        comment.setSynopsis(synopsis);
        commentRepository.save(comment);
    return CommentResponse.builder()
            .username(comment.getUser().getFullName())
            .comment(comment.getSynopsis())
            .build();
    }
    public void deleteComment(Long commentId, Authentication auth){
        var user=(User)auth.getPrincipal();
        var comment=commentRepository.findById(commentId)
                .orElseThrow(()->new EntityNotFound("Comment not found"));
        if (!comment.getCreatedBy().equals(user.getEmail())){
            throw  new OperationPermittedException("can not delete this comment");
        }
        commentRepository.delete(comment);
    }
    public CommentResponse viewComment(Long commentId){
        var comment=commentRepository.findById(commentId)
                .orElseThrow(()->new EntityNotFound("comment not found"));
        return CommentResponse.builder()
                .comment(comment.getSynopsis())
                .username(comment.getUser().getFullName())
                .build();
    }

}
