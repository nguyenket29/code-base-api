package com.java;

import com.java.entities.auth.RoleEntity;

import java.util.Optional;

public interface RoleService {
    Optional<RoleEntity> findByCode(String code);
}
