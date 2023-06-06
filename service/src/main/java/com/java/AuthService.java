package com.java;

import com.java.entities.auth.RefreshTokenEntity;
import com.java.entities.auth.ResetPasswordToken;
import com.java.entities.auth.UserEntity;
import com.java.request.*;

import java.util.Optional;

public interface AuthService {
    Optional<UserEntity> registerUser(RegistryRequest request);

    Optional<UserEntity> resetPassword(PasswordResetRequest passwordResetRequest);

    Optional<ResetPasswordToken> generatePasswordResetToken(PasswordResetLinkRequest passwordResetLinkRequest);

    Optional<RefreshTokenEntity> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest);

    Optional<UserEntity> updatePassword(UpdatePasswordRequest updatePasswordRequest);
}
