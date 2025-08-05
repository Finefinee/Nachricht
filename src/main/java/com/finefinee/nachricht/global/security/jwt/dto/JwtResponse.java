package com.finefinee.nachricht.global.security.jwt.dto;

public record JwtResponse(
        String accessToken,
        String refreshToken
) {
}
