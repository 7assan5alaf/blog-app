package com.hks.blog.controller;

import com.hks.blog.service.AdminService;
import com.hks.blog.service.PostService;
import com.hks.blog.service.ReportService;
import com.hks.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final PostService postService;
    private final ReportService reportService;
    private final AdminService adminService;

    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, Authentication auth) {
        postService.deletePost(postId,auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view-reports")
    public ResponseEntity<?>viewAllReports() {
        return ResponseEntity.ok().body(reportService.findAll());
    }
    @PutMapping("/block-user/{userId}")
    public ResponseEntity<?>blockUser(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.blockUser(userId));
    }

}
