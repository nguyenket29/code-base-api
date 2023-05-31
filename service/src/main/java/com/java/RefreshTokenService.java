package com.java;

import com.java.entities.auth.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenEntity> findByToken(String token);
    RefreshTokenEntity save(RefreshTokenEntity refreshTokenEntity);
    void deleteById(String id);
    void increaseCount(RefreshTokenEntity refreshTokenEntity);
    RefreshTokenEntity createRefreshToken();
}
