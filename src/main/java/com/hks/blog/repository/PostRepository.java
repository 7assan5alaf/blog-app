package com.hks.blog.repository;

import com.hks.blog.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("""
    SELECT p from Post p where p.user.id=:id
""")
    List<Post> findByUserId(Long id);

}
