package com.finefinee.nachricht.global.security.jwt.refresh;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUsername(String username);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsername(String username);

}
