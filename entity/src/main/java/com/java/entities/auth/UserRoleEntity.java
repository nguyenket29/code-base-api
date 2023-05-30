package com.java.entities.auth;

import com.java.AbstractAuditingEntity;

import javax.persistence.*;

@Entity
@Table(name = "users_roles")
public class UserRoleEntity extends AbstractAuditingEntity {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_id")
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
