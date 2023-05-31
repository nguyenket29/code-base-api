package com.java.auth;

import com.java.UserService;
import com.java.constant.ErrorConstants;
import com.java.entities.auth.CustomUser;
import com.java.entities.auth.RoleEntity;
import com.java.exception.APIException;
import com.java.util.JsonUtil;
import com.java.util.JwtUtil;
import com.java.util.StringUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.java.common.Commons.*;
import static com.java.constant.ErrorConstants.ACCESS_DENIED;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserService authService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService authService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(AUTH_HEADER);
        if (StringUtil.isEmpty(token) || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI().substring(request.getContextPath().length());
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token, path);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorConstants.UNAUTHORIZED);
            return;
        }

        log.info("\n\n ===>>> When before update SecurityContextHolder. Authentication: {} \n\n", JsonUtil.toJson(SecurityContextHolder.getContext().getAuthentication()));
        log.info("\n\n ===>>> Updating SecurityContextHolder to contain Authentication ... \n\n");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("\n\n ===>>> When after update SecurityContextHolder. Authentication: {} \n\n", JsonUtil.toJson(SecurityContextHolder.getContext().getAuthentication()));
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token, String path) {
        String parseToken = parseJwt(token);
        if (jwtUtil.validateJwtToken(parseToken)) {
            Claims claims = jwtUtil.extractAllClaims(parseToken);
            String username = claims.get("userName", String.class);
            String userId = claims.get("uid", String.class);
            String fullName = claims.get("fullName", String.class);

            if (!checkPath(userId, path)) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withCode(ACCESS_DENIED);
            }

            List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(userId);
            UserDetails userDetails = new CustomUser(username,
                    "no-password",
                    true,
                    true,
                    true,
                    true,
                    grantedAuthorities,
                    userId,
                    fullName);
            return new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
        }
        return null;
    }

    private String parseJwt(String token) {
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length()).trim();
        }
        return null;
    }

    private boolean checkPath(String userId, String path) throws APIException {
        if (StringUtil.isEmpty(path)) {
            return false;
        }

        List<String> paths = authService.getRoleByUserId(userId).parallelStream()
                .map(RoleEntity::getPath).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(paths)) {
            return paths.stream().anyMatch(path::equals);
        }
        return false;
    }

    private List<GrantedAuthority> getGrantedAuthorities(String userId) throws APIException {
        List<String> roles = authService.getRoleByUserId(userId).parallelStream()
                .map(RoleEntity::getPath).collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(auth -> {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + auth.toUpperCase());
                grantedAuthorities.add(authority);
            });
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + DEFAULT_ROLE));
        }

        return grantedAuthorities;
    }
}
