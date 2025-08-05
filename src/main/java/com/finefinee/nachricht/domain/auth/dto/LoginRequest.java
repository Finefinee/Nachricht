package com.finefinee.nachricht.domain.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
