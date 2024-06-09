package com.hks.blog.controller;

import com.hks.blog.service.CommentService;
import com.hks.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome in our site");
    }
    @GetMapping("/posts")
    public ResponseEntity<?>viewAllPosts(@RequestParam(defaultValue = "0",required = false) int page,
                                         @RequestParam(defaultValue = "10",required = false) int size) {
        return ResponseEntity.ok(postService.findAll(page, size));
    }
    @GetMapping("/post/view/{id}")
    public ResponseEntity<?>viewPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.viewPost(id));
    }
    @GetMapping("/comments/{postId}")
    public ResponseEntity<?>viewAllCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.viewAllCommentByPost(postId));
    }
    @GetMapping("/comment/view/{commentId}")
    public ResponseEntity<?>viewComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.viewComment(commentId));
    }


}
