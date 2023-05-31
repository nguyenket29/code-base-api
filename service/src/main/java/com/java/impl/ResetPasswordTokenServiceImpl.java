package com.java.impl;

import com.java.ResetPasswordTokenService;
import com.java.auth.ResetPasswordTokenRepository;
import com.java.entities.auth.ResetPasswordToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordTokenServiceImpl(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Override
    public ResetPasswordToken save(ResetPasswordToken resetPasswordToken) {
        return resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public Optional<ResetPasswordToken> findByToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }
}
