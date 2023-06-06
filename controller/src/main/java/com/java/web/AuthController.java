package com.java.web;

import com.java.AuthService;
import com.java.entities.auth.RefreshTokenEntity;
import com.java.entities.auth.ResetPasswordToken;
import com.java.entities.auth.UserEntity;
import com.java.request.*;
import com.java.response.APIResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(value = "Auth Controller", description = "Các APIs xác thực")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registry")
    public ResponseEntity<APIResponse<Optional<UserEntity>>> registry(@RequestBody RegistryRequest request) {
        return ResponseEntity.ok(APIResponse.ok(authService.registerUser(request)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<APIResponse<Optional<UserEntity>>> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        return ResponseEntity.ok(APIResponse.ok(authService.resetPassword(passwordResetRequest)));
    }

    @PostMapping("/generate-token-reset-password")
    public ResponseEntity<APIResponse<Optional<ResetPasswordToken>>>
                    generatePasswordResetToken(@RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {
        return ResponseEntity.ok(APIResponse.ok(authService.generatePasswordResetToken(passwordResetLinkRequest)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<APIResponse<Optional<RefreshTokenEntity>>>
                    refreshJwtToken(@RequestBody TokenRefreshRequest passwordResetRequest) {
        return ResponseEntity.ok(APIResponse.ok(authService.refreshJwtToken(passwordResetRequest)));
    }

    @PostMapping("/update-password")
    public ResponseEntity<APIResponse<Optional<UserEntity>>> updatePassword(@RequestBody UpdatePasswordRequest passwordResetRequest) {
        return ResponseEntity.ok(APIResponse.ok(authService.updatePassword(passwordResetRequest)));
    }
}
