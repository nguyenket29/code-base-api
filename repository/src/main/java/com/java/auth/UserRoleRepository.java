package com.java.auth;

import com.java.entities.auth.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    List<UserRoleEntity> findAllByUserId(String userId);
}
