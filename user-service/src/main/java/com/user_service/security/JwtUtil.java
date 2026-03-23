package com.user_service.security;

import com.user_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "njoyholidayz1user2service3secretkey4ketul2026";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUserId().toString())   // VERY IMPORTANT
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 hours
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // 1 MINUTE
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        System.out.println("Reached");
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String userId) {
        final String extractedUserId = extractSubject(token);
        return (extractedUserId.equals(userId) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public String extractRole(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

}
