package com.finefinee.nachricht.domain.auth.service;

import com.finefinee.nachricht.domain.auth.dto.LoginRequest;
import com.finefinee.nachricht.domain.auth.dto.RefreshRequest;
import com.finefinee.nachricht.domain.auth.dto.RegisterRequest;
import com.finefinee.nachricht.domain.user.entity.UserEntity;
import com.finefinee.nachricht.domain.user.entity.UserRole;
import com.finefinee.nachricht.domain.user.repository.UserRepository;
import com.finefinee.nachricht.global.security.jwt.dto.JwtResponse;
import com.finefinee.nachricht.global.security.jwt.provider.JwtProvider;
import com.finefinee.nachricht.global.security.jwt.refresh.RefreshToken;
import com.finefinee.nachricht.global.security.jwt.refresh.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void register(RegisterRequest request) {
        if (userRepository.existsById(request.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자");
        }

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .profileImageUrl(null)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    public JwtResponse login(LoginRequest request) {
        UserEntity userEntity = userRepository.findById(request.username())
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));

        if (!passwordEncoder.matches(request.password(), userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String username = request.username();
        String accessToken = jwtProvider.generateAccessToken(username);
        String refreshToken = jwtProvider.generateRefreshToken(username);

        refreshTokenRepository.deleteById(username);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .username(username)
                .token(refreshToken) // BCrypt 인코딩 제거
                .expiry(jwtProvider.getExpire(refreshToken))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return new JwtResponse(accessToken, refreshToken);
    }

    @Transactional
    public JwtResponse refresh(RefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = jwtProvider.getUsername(refreshToken);

        RefreshToken savedRefreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토큰입니다."));

        if (!refreshToken.equals(savedRefreshToken.getToken())) { // equals 비교로 변경
            throw new RuntimeException("유효하지 않은 토큰");
        }

        jwtProvider.validateToken(refreshToken);

        String newAccessToken = jwtProvider.generateAccessToken(savedRefreshToken.getUsername());

        return new JwtResponse(newAccessToken, refreshToken);
    }
}