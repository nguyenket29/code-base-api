package com.java.web;

import com.java.UserService;
import com.java.entities.auth.UserEntity;
import com.java.response.APIResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "User Controller", description = "Các APIs quản lý người dùng")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserEntity>>> getAll() {
        return ResponseEntity.ok(APIResponse.ok(userService.findAll()));
    }
}
