package com.hks.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${spring.security.secret-key}")
    private String secretKey;
    @Value("${spring.security.expiration-date}")
    private long expirationDate;
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        var claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails, Map<String,Object>claims){
        return buildToken(userDetails,claims,expirationDate);
    }

    private String buildToken(UserDetails userDetails, Map<String, Object> claims, long expirationDate) {
        var authorities=userDetails.getAuthorities().stream().map(
                GrantedAuthority::getAuthority
        ).toList();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expirationDate))
                .claim("authorities",authorities)
                .signWith(getKey()).compact();
    }

    public boolean isValidToken(String token,UserDetails userDetails){
        var username=extractUsername(token);
        return username.equals(userDetails.getUsername())&&!isExpirationToken(token);
    }

    private boolean isExpirationToken(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

    private Key getKey() {
        byte []key= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }
}
