package com.hks.blog.service;

import com.hks.blog.exception.EntityNotFound;
import com.hks.blog.repository.UserRepository;
import com.hks.blog.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    public MessageResponse blockUser(Long userId) {
        var user=userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFound("User Not Found"));
        user.setEnable(false);
        userRepository.save(user);
        return MessageResponse.builder()
                .message("User blocked")
                .build();
    }
}
