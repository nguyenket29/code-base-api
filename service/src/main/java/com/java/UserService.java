package com.java;

import com.java.entities.auth.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByUserName(String username);
    List<String> getRoleByUserId(String userId);
}
