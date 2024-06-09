package com.hks.blog.service;


import com.hks.blog.entity.Post;
import com.hks.blog.entity.User;
import com.hks.blog.exception.EntityNotFound;

import com.hks.blog.exception.OperationPermittedException;
import com.hks.blog.mapper.PostMapper;
import com.hks.blog.repository.CommentRepository;
import com.hks.blog.repository.PostRepository;
import com.hks.blog.response.MessageResponse;
import com.hks.blog.response.PageResponse;
import com.hks.blog.response.PostResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentRepository commentRepository;
    @Value("${spring.upload.image}")
    private String finalFilePath;

    public PostResponse createPost(@NotBlank(message = "you should enter title") String title,
                                   @NotBlank(message = "you should enter synopsis") String synopsis,
                                   MultipartFile image,
                                   Authentication authentication){
        var user=(User)authentication.getPrincipal();
        var post= Post.builder()
                .synopsis(synopsis)
                .title(title)
                .comments(new ArrayList<>())
                .user(user)
                .likeCount(0)
                .viewCount(0)
                .build();
        postRepository.save(post);
        post.setImage(uploadFile(post.getId(),image));
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PageResponse<PostResponse>findAll(int number,int size){
        Pageable pageable=PageRequest.of(number,size, Sort.by("createdAtDate").descending());
        Page<Post> posts=postRepository.findAll(pageable);
        List<PostResponse>postResponses=posts
                .stream().map(postMapper::toPostResponse)
                .toList();
        return new PageResponse<>(postResponses,posts.getTotalElements()
                ,posts.getTotalPages(),posts.isFirst(),posts.isLast());
    }

    public PostResponse viewPost(Long id){
       var post= postRepository.findById(id)
                .orElseThrow(()-> new EntityNotFound("Post Not Found"));
       post.setViewCount(post.getViewCount()+1);
       postRepository.save(post);
       return postMapper.toPostResponse(post);
    }

    public void likeToPost(Long postId){
        var post= postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFound("Post Not Found"));
        post.setLikeCount(post.getLikeCount()+1);
        postRepository.save(post);
    }

   public PostResponse updatePost(Long postId,String title,String synopsis,Authentication auth){
        var user=(User)auth.getPrincipal();
        var post= postRepository.findById(postId)
               .orElseThrow(()-> new EntityNotFound("Post Not Found"));
       if(!post.getUser().equals(user)){
           throw new OperationPermittedException("You can not update this post because not owen it");
       }
       post.setSynopsis(synopsis);
       post.setTitle(title);
       postRepository.save(post);
       return postMapper.toPostResponse(post);
   }

   public void deletePost(Long id,Authentication auth){
        var user=(User)auth.getPrincipal();
       var post= postRepository.findById(id)
               .orElseThrow(()-> new EntityNotFound("Post Not Found"));
       if (!post.getUser().getEmail().equals(user.getEmail())){
           throw new OperationPermittedException("can not delete this post because not owen it");
       }
       commentRepository.deleteAllByPostId(post.getId());
       postRepository.delete(post);
   }

    public MessageResponse updateImage(Long postId, MultipartFile image, Authentication auth) {
        var post=postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFound("Post Not Found"));
        var user=(User)auth.getPrincipal();
        if (!post.getUser().equals(user)){
            throw new OperationPermittedException("You can not update this post because not owen it");
        }
        post.setImage(uploadFile(post.getId(),image));
        postRepository.save(post);
        return MessageResponse.builder()
                .message("Update success")
                .build();
    }

   //upload file

    public String uploadFile(Long postId,MultipartFile image){
        var post= postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFound("Post Not Found"));

         final String finalPath=finalFilePath+ File.separator+postId;
        File folder=new File(finalPath);
        if (!folder.exists()){
            boolean createFolder=folder.mkdirs();
            if (!createFolder){
                return "";
            }
        }

        var fileExtension=getExtensionFile(image.getOriginalFilename());
        var fileName= folder+File.separator+System.currentTimeMillis()+fileExtension;
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

    private String getExtensionFile(String originalFilename) {
        if(originalFilename.isEmpty()||originalFilename==null){
            return "";
        }
        int dotIndex=originalFilename.indexOf(".");
        if (dotIndex == -1)
            return "";
        return originalFilename.substring(dotIndex);

    }
    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl))
            return null;

        try {
            Path path = new File(fileUrl).toPath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("No file found in the path{}" + e.getMessage());
        }
        return null;

    }


}
