package com.taskwave.taskwave.dto;

public record AuthTokens(
        String accessToken,
        String refreshToken
) {}