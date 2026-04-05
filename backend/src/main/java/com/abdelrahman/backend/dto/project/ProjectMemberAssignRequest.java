package com.abdelrahman.backend.dto.project;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMemberAssignRequest {

    @NotNull(message = "Project id is required")
    private Long projectId;

    @NotNull(message = "User id is required")
    private Long userId;
}