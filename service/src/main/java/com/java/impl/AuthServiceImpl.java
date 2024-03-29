package com.java.impl;

import com.java.*;
import com.java.context.SystemContextHolder;
import com.java.entities.auth.*;
import com.java.exception.APIException;
import com.java.mapper.UserMapper;
import com.java.request.*;
import com.java.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.java.constant.Constants.DEFAULT_ROLE;
import static com.java.constant.ErrorConstants.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final ResetPasswordTokenService resetPasswordTokenService;

    @Value("${jwt.expiration}")
    private long expiration;

    public AuthServiceImpl(UserService userService, UserMapper userMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserRoleService userRoleService, RoleService roleService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, ResetPasswordTokenService resetPasswordTokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    /**
     * Create user with role default
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Optional<UserEntity> registerUser(RegistryRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw APIException.from(HttpStatus.CONFLICT).withCode(EMAIL_EXIST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setStatus(UserEntity.Status.ACTIVE);
        UserEntity userEntity = userService.save(user);

        // save userRole default
        Optional<RoleEntity> roleEntityOptional = roleService.findByCode(DEFAULT_ROLE);
        if (roleEntityOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withCode(USER_NOT_FOUND);
        }

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setRoleId(roleEntityOptional.get().getId());
        userRoleEntity.setUserId(userEntity.getId());
        userRoleService.save(userRoleEntity);

        return Optional.of(userEntity);
    }

    /**
     * Reset a password given a reset request and return the updated user
     */
    @Override
    @Transactional
    public Optional<UserEntity> resetPassword(PasswordResetRequest passwordResetRequest) {
        String token = passwordResetRequest.getToken();
        Optional<ResetPasswordToken> resetPasswordTokenOptional = resetPasswordTokenService.findByToken(token);

        if (resetPasswordTokenOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND);
        }

        if (jwtUtil.isTokenExpired(token)) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withCode(TOKEN_EXPIRE);
        }

        final String encodedPassword = bCryptPasswordEncoder.encode(passwordResetRequest.getPassword());
        Optional<UserEntity> userEntity = userService.findById(resetPasswordTokenOptional.get().getUserId());
        if (userEntity.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withCode(USER_NOT_FOUND);
        }
        userEntity.get().setPassword(encodedPassword);

        return Optional.of(userService.save(userEntity.get()));
    }

    /**
     * Generates a password reset token from the given reset request
     */
    @Override
    public Optional<ResetPasswordToken> generatePasswordResetToken(PasswordResetLinkRequest passwordResetLinkRequest) {
        String email = passwordResetLinkRequest.getEmail();
        return userService.findByEmail(email)
                .map(user -> {
                    ResetPasswordToken passwordResetToken = new ResetPasswordToken();
                    passwordResetToken.setToken(UUID.randomUUID().toString());
                    passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
                    passwordResetToken.setUserId(user.getId());
                    resetPasswordTokenService.save(passwordResetToken);
                    return Optional.of(passwordResetToken);
                }).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND)
                        .withMessage("No matching user found for the given request"));
    }

    @Override
    public Optional<RefreshTokenEntity> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        String refreshToken = jwtUtil.generateToken(customUser.getUserName(), customUser.getAuthorities(),
                customUser.getFullName(), customUser.getPaths(), customUser.getId());
        return Optional.of(refreshTokenService.findByToken(refreshToken)
            .map(rf -> {
                if (jwtUtil.isTokenExpired(refreshToken)) {
                    throw APIException.from(HttpStatus.BAD_REQUEST).withCode(UNAUTHORIZED);
                }
                refreshTokenService.increaseCount(rf);
                return rf;
            }).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND)
                    .withMessage("Missing refresh token in database.Please login again")));
    }

    @Override
    public Optional<UserEntity> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Optional<UserEntity> userEntity = userService.findById(customUser.getId());
        if (userEntity.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withCode(USER_NOT_FOUND);
        }

        if (!bCryptPasswordEncoder.matches(userEntity.get().getPassword(),
                updatePasswordRequest.getOldPassword())) {
            throw APIException.from(HttpStatus.BAD_REQUEST);
        }

        String newPassword = bCryptPasswordEncoder.encode(updatePasswordRequest.getNewPassword());
        userEntity.get().setPassword(newPassword);
        userService.save(userEntity.get());

        return Optional.of(userEntity.get());
    }
}
