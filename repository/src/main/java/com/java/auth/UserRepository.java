package com.java.auth;

import com.java.entities.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the EntityAuditEvent entity.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameAndStatus(String username, Short status);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
