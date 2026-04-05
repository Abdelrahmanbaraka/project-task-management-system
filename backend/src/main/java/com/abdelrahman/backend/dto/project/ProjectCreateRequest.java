package com.abdelrahman.backend.dto.project;

import com.abdelrahman.backend.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectCreateRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "Created by user id is required")
    private Long createdByUserId;
}
