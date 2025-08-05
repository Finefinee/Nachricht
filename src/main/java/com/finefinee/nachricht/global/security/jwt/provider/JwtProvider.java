package com.finefinee.nachricht.global.security.jwt.provider;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.finefinee.nachricht.domain.user.entity.UserEntity;
import com.finefinee.nachricht.domain.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;

    private final String secret = "MGRqMm52bnZqYWoya3ZrYmxua3NqdjhidkBwYThsbjRpYWl2MnZyNnZlY2RyeGprOWI5YQ==";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    public String generateAccessToken(String username) {

        UserEntity userEntity = userRepository.findById(username)
                        .orElseThrow(() -> new UsernameNotFoundException("user를 찾을 수 없습니다."));

        System.out.println(secretKey);
        return Jwts.builder()
                .subject(username)
                .claim("role", userEntity.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {

        UserEntity userEntity = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("user를 찾을 수 없습니다."));

        return Jwts.builder()
                .subject(username)
                .claim("role", userEntity.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(secretKey)
                .compact();
    }


    public Claims parseClaims(String token) {
        return Jwts.parser() // 쪼개기
                .verifyWith(secretKey) // 확인
                .build() // ?
                .parseSignedClaims(token) // ?
                .getPayload(); // 안에 들어간거
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject(); // payload 구하고 거기서 대표 claim 구하기
    }

    public Long getExpire(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token); // 이걸로 만료되었는지 확인함
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}