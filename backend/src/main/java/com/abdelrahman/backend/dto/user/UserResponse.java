package com.abdelrahman.backend.dto.user;

import com.abdelrahman.backend.enums.RoleName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Boolean active;
    private RoleName roleName;
}