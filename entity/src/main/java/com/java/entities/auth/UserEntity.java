
package com.java.entities.auth;

import com.java.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends AbstractAuditingEntity {
    public static final class Gender {
        public static final short MALE = 0;
        public static final short FEMALE = 1;
        public static final short OTHER = 2;
    }

    public static final class Status {
        public static final short ACTIVE = 1;
        public static final short WAITING = 0;
        public static final short LOCK = -1;
    }

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    private Short gender;

    @Column(name = "status")
    private Short status;

    @Column(name = "address")
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserEntity() {

    }
}
