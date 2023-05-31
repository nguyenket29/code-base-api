package com.java;

import com.java.entities.auth.RoleEntity;
import com.java.entities.auth.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByUserName(String username);
    List<RoleEntity> getRoleByUserId(String userId);
    Optional<UserEntity> findByUsernameAndStatus(String username, short status);
    List<UserEntity> findAll();
}
