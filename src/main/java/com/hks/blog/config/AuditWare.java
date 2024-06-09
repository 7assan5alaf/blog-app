package com.hks.blog.config;

import com.hks.blog.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditWare implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null||!authentication.isAuthenticated()||
          authentication instanceof AnonymousAuthenticationToken
        )
        return Optional.empty();
        var user=(User)authentication.getPrincipal();
        return Optional.ofNullable(user.getFullName());

    }
}
