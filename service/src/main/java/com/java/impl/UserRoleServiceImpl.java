package com.java.impl;

import com.java.UserRoleService;
import com.java.auth.UserRoleRepository;
import com.java.entities.auth.UserRoleEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void save(UserRoleEntity userRoleEntity) {
        userRoleRepository.save(userRoleEntity);
    }
}
