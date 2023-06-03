package com.java.web;

import com.java.AuthService;
import com.java.entities.auth.UserEntity;
import com.java.request.PasswordResetRequest;
import com.java.request.RegistryRequest;
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
}
