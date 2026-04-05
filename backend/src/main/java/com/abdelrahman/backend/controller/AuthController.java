package com.abdelrahman.backend.controller;

import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.security.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal SecurityUser securityUser) {
        User user = securityUser.getUser();

        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole().getName().name(),
                "active", user.getActive()
        );
    }
}