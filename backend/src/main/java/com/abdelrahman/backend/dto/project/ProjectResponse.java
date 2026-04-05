package com.abdelrahman.backend.dto.project;

import com.abdelrahman.backend.enums.ProjectStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean archived;
    private Long createdByUserId;
    private String createdByUsername;
}