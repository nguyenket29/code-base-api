package com.java.impl;

import com.java.*;
import com.java.entities.auth.*;
import com.java.exception.APIException;
import com.java.mapper.UserMapper;
import com.java.request.*;
import com.java.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
    private final ResetPasswordTokenService resetPasswordTokenService;

    public AuthServiceImpl(UserService userService, UserMapper userMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserRoleService userRoleService, RoleService roleService, JwtUtil jwtUtil, ResetPasswordTokenService resetPasswordTokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

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
        return Optional.empty();
    }

    @Override
    public Optional<String> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<RefreshTokenEntity> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginRequest loginRequest) {
        return Optional.empty();
    }
}
