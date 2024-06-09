package com.hks.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal
            (@NotNull HttpServletRequest request,
             @NotNull HttpServletResponse response,
             @NotNull FilterChain filterChain) throws ServletException, IOException {

          if(request.getContextPath().contains("/blog/auth")){
              filterChain.doFilter(request,response);
              return;
          }
          var auth=request.getHeader(HttpHeaders.AUTHORIZATION);
          if(auth==null ||!auth.startsWith("Bearer ")){
              filterChain.doFilter(request,response);
              return;
          }
          var token=auth.substring(7);
          var username=jwtService.extractUsername(token);
          if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
              var details=userDetailsService.loadUserByUsername(username);
              UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(details
              ,null,details.getAuthorities()
              );
              authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
          filterChain.doFilter(request,response);
    }
}
