package com.java.auth;

import com.java.entities.auth.CustomUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

import static com.java.constant.Constants.ANONYMOUS;

public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return Optional.of(ANONYMOUS);
        }
        Object principal = authentication.getPrincipal();
        if (Objects.isNull(principal)) {
            return Optional.of(ANONYMOUS);
        }
        if (principal instanceof CustomUser) {
            return Optional.of(((CustomUser) principal).getUsername());
        }
        return Optional.of(ANONYMOUS);
    }
}
