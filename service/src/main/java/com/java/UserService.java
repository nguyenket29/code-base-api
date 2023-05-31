package com.java;

import com.java.entities.auth.RoleEntity;
import com.java.entities.auth.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findById(String id);
    UserEntity save(UserEntity user);
    List<RoleEntity> getRoleByUserId(String userId);
    Optional<UserEntity> findByUsernameAndStatus(String username, short status);
    List<UserEntity> findAll();
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
