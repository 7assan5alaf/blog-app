package com.hks.blog.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hks.blog.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("""
         select c from Comment c where c.post.id=:postId
          """)
    List<Comment>findAllByPostId(Long postId);
    @Modifying
    @Transactional
    @Query("""
        DELETE from Comment c where c.post.id=:postId
       """)
    void deleteAllByPostId(Long postId);
}
