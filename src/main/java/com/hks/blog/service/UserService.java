package com.hks.blog.service;

import com.hks.blog.dto.ChangePasswordDto;
import com.hks.blog.entity.User;
import com.hks.blog.exception.EntityNotFound;
import com.hks.blog.exception.OperationPermittedException;
import com.hks.blog.mapper.PostMapper;
import com.hks.blog.repository.PostRepository;

import com.hks.blog.repository.UserProfileRepository;
import com.hks.blog.repository.UserRepository;
import com.hks.blog.response.MessageResponse;
import com.hks.blog.response.PostResponse;
import com.hks.blog.response.UserProfileResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.upload.photo}")
    private String pathLocationImage;
    public UserProfileResponse updateUserProfile(MultipartFile profileImage, String bio,
                                                 String address, String link, Authentication auth) {
        var user=(User)auth.getPrincipal();
        var profile=userProfileRepository.findByCreatedBy(user.getEmail())
                .orElseThrow(()->new EntityNotFound("User Not Found"));
        profile.setBio(bio);
        profile.setAddress(address);
        profile.setImage(uploadImage(profileImage,auth));
        profile.setLink(link);
        userProfileRepository.save(profile);
        return UserProfileResponse
                .builder()
                .bio(profile.getBio())
                .email(profile.getUser().getEmail())
                .link(profile.getLink())
                .name(profile.getUser().getFullName())
                .location(profile.getAddress())
                .phone(profile.getUser().getPhoneNumber())
                .profileImage(readImage(profile.getImage()))
                .build();
    }

      public UserProfileResponse viewProfile(Long userId) {
        var user=userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFound("User Not Found"));
        var profile=userProfileRepository.findByCreatedBy(user.getEmail())
                .orElseThrow(()->new EntityNotFound("User Not Found"));
        var posts=postRepository.findByUserId(userId);
          List<PostResponse>responses=posts.stream()
                  .map(postMapper::toPostResponse)
                  .toList();

          return UserProfileResponse.builder()
                  .profileImage(readImage(profile.getImage()))
                  .phone(user.getPhoneNumber())
                  .bio(profile.getBio())
                  .link(profile.getLink())
                  .location(profile.getAddress())
                  .name(user.getFullName())
                  .email(user.getEmail())
                  .posts(responses)
                  .build();
      }


    public MessageResponse changePassword(ChangePasswordDto passwordDto, Authentication auth) {
        var user=(User)auth.getPrincipal();
        if(!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new OperationPermittedException("old password not match");
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            throw new OperationPermittedException("new password and confirm password not match");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        return MessageResponse.builder()
                .message("Password changed")
                .build();
    }





    //upload image
    public String uploadImage(MultipartFile image, Authentication auth) {
        var user=(User)auth.getPrincipal();
        final String finalPath = pathLocationImage+ File.separator+user.getId();
        File profileImage = new File(finalPath);
        if(!profileImage.exists()){
            boolean createFolder = profileImage.mkdirs();
            if(!createFolder){
                return null;
            }
        }
        var fileExtension=getFileExtension(image.getOriginalFilename());
        var fileName=profileImage+File.separator+System.currentTimeMillis()+fileExtension;
        Path path= Paths.get(fileName);

        try {
            Files.write(path, image.getBytes());
            log.info("Save file in " + fileName);
            return fileName;
        } catch (IOException e) {
            log.error("file not save" + e.getMessage());
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename==null&&!originalFilename.isEmpty()){
            return "";
        }
        int dot = originalFilename.lastIndexOf('.');
        if(dot == -1){
            return "";
        }
        return originalFilename.substring(dot);
    }
    public static byte[]readImage(String path){
        if (StringUtils.isBlank(path))
            return null;

        try {
            Path url = new File(path).toPath();
            return Files.readAllBytes(url);
        } catch (IOException e) {
            log.error("No file found in the path{}" + e.getMessage());
        }
        return null;
    }


}
