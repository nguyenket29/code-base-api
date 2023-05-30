package com.java.auth;

import com.java.entities.auth.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the EntityAuditEvent entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    List<RoleEntity> findAllByIdIn(List<String> roleIds);
}
