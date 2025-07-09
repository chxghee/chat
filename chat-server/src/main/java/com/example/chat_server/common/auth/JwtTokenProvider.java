package com.example.chat_server.common.auth;

import com.example.chat_server.exception.ApplicationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long expiration;
    private final Key SECRET_KEY;

    public JwtTokenProvider(@Value("${spring.jwt.secretKey}") String secretKey,
                            @Value("${spring.jwt.expiration}") int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration * 60 * 1000L;
        this.SECRET_KEY = new SecretKeySpec(
                java.util.Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName()
        );
    }

    public String createToken(String email, String role) {
        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthExceptionCode.ACCESS_TOKEN_EXPIRED);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthExceptionCode.UNAUTHENTICATED_REQUEST);
        } catch (JwtException e) {
            throw new ApplicationException(AuthExceptionCode.ACCESS_TOKEN_INVALID);
        }
    }

    public String getAccessToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank() || !bearerToken.startsWith("Bearer ")) {
            throw new ApplicationException(AuthExceptionCode.ACCESS_TOKEN_ILLEGAL);
        }
        return bearerToken.substring(7);
    }

}
