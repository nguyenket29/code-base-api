package com.java.impl;

import com.java.UserService;
import com.java.auth.RoleRepository;
import com.java.auth.UserRepository;
import com.java.auth.UserRoleRepository;
import com.java.entities.auth.RoleEntity;
import com.java.entities.auth.UserEntity;
import com.java.entities.auth.UserRoleEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Cacheable("users")
    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable("users")
    public List<RoleEntity> getRoleByUserId(String userId) {
        List<String> roleIds = userRoleRepository.findAllByUserId(userId)
                .stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(roleIds)) {
            return roleRepository.findAllByIdIn(roleIds);
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable("users")
    public Optional<UserEntity> findByUsernameAndStatus(String username, short status) {
        return userRepository.findByUsernameAndStatus(username, status);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
