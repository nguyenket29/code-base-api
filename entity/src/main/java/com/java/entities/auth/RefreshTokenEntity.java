package com.java.entities.auth;

import com.java.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity extends AbstractAuditingEntity {
    @Column(name = "user_id")
    private Integer userId;

    @Lob
    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "refresh_count")
    private Long refreshCount;

    public Long getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(Long refreshCount) {
        this.refreshCount = refreshCount;
    }

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
