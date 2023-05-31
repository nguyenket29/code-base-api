package com.java.impl;

import com.java.RefreshTokenService;
import com.java.auth.RefreshTokenRepository;
import com.java.entities.auth.RefreshTokenEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public void deleteById(String id) {
        refreshTokenRepository.deleteById(id);
    }

    @Override
    public void increaseCount(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenEntity.incrementRefreshCount();
        this.save(refreshTokenEntity);
    }

    /**
     * Creates and returns a new refresh token
     */
    @Override
    public RefreshTokenEntity createRefreshToken() {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }
}
