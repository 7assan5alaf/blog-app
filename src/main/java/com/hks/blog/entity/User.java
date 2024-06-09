package com.hks.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue
    private  Long id;

    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private boolean enable;
    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @OneToMany(mappedBy = "user")
    private List<Post>posts;
    @OneToMany(mappedBy = "user")
    private List<OtpCode>codes;
    
    @OneToMany(mappedBy = "user")
    private List<Comment>comments;
    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
