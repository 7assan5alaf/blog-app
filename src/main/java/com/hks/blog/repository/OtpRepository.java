package com.hks.blog.repository;

import com.hks.blog.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpCode,Long> {

    Optional<OtpCode>findByOtp(String otp);
}
