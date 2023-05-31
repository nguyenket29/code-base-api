package com.java.impl;

import com.java.UserService;
import com.java.entities.auth.CustomUser;
import com.java.entities.auth.RoleEntity;
import com.java.entities.auth.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userService
                .findByUsernameAndStatus(username, UserEntity.Status.ACTIVE);
        if (userEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        UserEntity user = userEntityOptional.get();
        List<RoleEntity> roles = userService.getRoleByUserId(user.getId());

        CustomUser customUser = new CustomUser(username, user.getPassword(), true, true,
                true, true, getGrantedAuthorities(roles),
                user.getId(), user.getFullName());
        customUser.setPaths(getPaths(roles));
        return customUser;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<RoleEntity> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    private List<String> getPaths(List<RoleEntity> roles) {
        List<String> paths = new ArrayList<>();
        for (RoleEntity role : roles) {
            paths.add(role.getPath());
        }
        return paths;
    }
}
