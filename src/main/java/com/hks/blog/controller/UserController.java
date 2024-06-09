package com.hks.blog.controller;

import com.hks.blog.dto.ChangePasswordDto;
import com.hks.blog.dto.CommentDto;
import com.hks.blog.service.CommentService;
import com.hks.blog.service.PostService;
import com.hks.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    //Manage post
    @PostMapping("/post/create")
    public ResponseEntity<?>createPost(@RequestParam MultipartFile image, @RequestParam String title,
                                       @RequestParam String synopsis, Authentication auth) {
        return ResponseEntity.ok(postService.createPost(title,synopsis,image,auth));
    }

    @DeleteMapping("/post/delete/{id}")
    public ResponseEntity<?>deletePost(@PathVariable Long id, Authentication auth) {
        postService.deletePost(id,auth);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/post/update/{postId}")
    public ResponseEntity<?>updatePost(@PathVariable Long postId, @RequestParam String title,
                                       @RequestParam String synopsis, Authentication auth){
        return ResponseEntity.ok(postService.updatePost(postId,title,synopsis,auth));
    }
    @PostMapping("/post/update-image/{postId}")
    public ResponseEntity<?>updateImage(@PathVariable Long postId,@RequestParam MultipartFile image, Authentication auth){
        return ResponseEntity.ok(postService.updateImage(postId,image,auth));
    }
    @PutMapping("/post/like/{postId}")
    public ResponseEntity<?>likeToPost(@PathVariable Long postId){
        postService.likeToPost(postId);
        return ResponseEntity.ok().build();
    }

    // Manage profile
    @PutMapping("/update-profile")
    public ResponseEntity<?>updateProfile(@RequestParam MultipartFile image
            ,@RequestParam String bio, @RequestParam String address,
                                          @RequestParam String link, Authentication auth){
        return ResponseEntity.ok(userService.updateUserProfile(image,bio,address,link,auth));
    }
    @GetMapping("/view-profile/{id}")
    public ResponseEntity<?>viewProfile(@PathVariable Long id){
        return ResponseEntity.ok(userService.viewProfile(id));
    }
    @PutMapping("/change-password")
    public ResponseEntity<?>changePassword(@RequestBody ChangePasswordDto passwordDto, Authentication auth){
        return ResponseEntity.ok(userService.changePassword(passwordDto,auth));
    }

    // Manage Comment
    @PostMapping("/comment/add")
    public ResponseEntity<?>addComment(@RequestBody CommentDto commentDto, Authentication auth){
        return ResponseEntity.ok(commentService.addComment(commentDto,auth));
    }

    @PutMapping("/comment/update/{commentId}")
    public ResponseEntity<?>updateComment(@PathVariable Long commentId,@RequestParam String synopsis, Authentication auth){
        return ResponseEntity.ok(commentService.updateComment(commentId,synopsis,auth));
    }
    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<?>deleteComment(@PathVariable Long commentId,Authentication auth){
        commentService.deleteComment(commentId,auth);
        return ResponseEntity.ok().build();
    }


}
