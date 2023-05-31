package com.java.impl;

import com.java.RoleService;
import com.java.auth.RoleRepository;
import com.java.entities.auth.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.java.constant.Constants.DEFAULT_ROLE;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<RoleEntity> findByCode(String code) {
        return roleRepository.findByCode(code);
    }
}
