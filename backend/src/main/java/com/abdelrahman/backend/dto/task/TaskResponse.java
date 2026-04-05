package com.abdelrahman.backend.dto.task;

import com.abdelrahman.backend.enums.TaskPriority;
import com.abdelrahman.backend.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private Long createdByUserId;
    private String createdByUsername;
    private Long assignedToUserId;
    private String assignedToUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}