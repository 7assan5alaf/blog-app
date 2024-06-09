package com.hks.blog.repository;

import com.hks.blog.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserProfileRepository extends JpaRepository <UserProfile,Long> {
     @Query("""
        select u from UserProfile u where u.createdBy=:email
        """)
    Optional<UserProfile> findByCreatedBy(String email);
}
