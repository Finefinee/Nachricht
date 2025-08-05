package com.finefinee.nachricht.global.security.jwt.provider;

import com.finefinee.nachricht.domain.user.entity.UserEntity;
import com.finefinee.nachricht.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;

    private final String secret = "17db38ab0c217d6e14bdc6ae09cb61e517da1e69e6bb1a8ece983368e0921695e5f678619a0799b6436b3422d9de95cbdbb056e5b715e0fa15e5e4145e6ad77cb09d519fe48685e9d5a2e57c93acdb48e6855ed3d906155c4175e32cc4332f10bf9764ee15cc20804b0f509e498c0c6b9d6dc28bc773b794c30a4091c874f68b8a7f73211761b220b63c5004753d02aefefcd9c89696dabb89f0bbafd5b229396bfff383d513e8d0f8cce4456a82042deb4c1068b00dee28098fb9c77d5f89fe1b4f57c61de8fcb3e9d60d8bb048232b7860041267d40e607faf12150cd7a6091cdcf1271427669106c7b932de83559e5ccb5c843d7b33476f8adb5c05853af2";
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