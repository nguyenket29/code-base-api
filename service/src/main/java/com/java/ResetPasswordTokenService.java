package com.java;

import com.java.entities.auth.ResetPasswordToken;

import java.util.Optional;

public interface ResetPasswordTokenService {
    ResetPasswordToken save(ResetPasswordToken resetPasswordToken);
    Optional<ResetPasswordToken> findByToken(String token);
}
