package com.abdelrahman.backend.security;

import com.abdelrahman.backend.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser securityUser)) {
            throw new RuntimeException("No authenticated user found");
        }

        return securityUser.getUser();
    }
}