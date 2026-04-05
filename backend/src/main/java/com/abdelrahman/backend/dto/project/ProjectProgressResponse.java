package com.abdelrahman.backend.dto.project;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectProgressResponse {
    private Long projectId;
    private String projectName;
    private long totalTasks;
    private long completedTasks;
    private double progressPercentage;
}