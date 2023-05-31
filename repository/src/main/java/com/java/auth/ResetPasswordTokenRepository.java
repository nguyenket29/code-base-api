package com.java.auth;

import com.java.entities.auth.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
    Optional<ResetPasswordToken> findByToken(String token);
}
