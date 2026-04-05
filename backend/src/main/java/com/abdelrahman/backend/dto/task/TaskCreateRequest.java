package com.abdelrahman.backend.dto.task;

import com.abdelrahman.backend.enums.TaskPriority;
import com.abdelrahman.backend.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Task priority is required")
    private TaskPriority priority;

    @NotNull(message = "Project id is required")
    private Long projectId;

    @NotNull(message = "Created by user id is required")
    private Long createdByUserId;

    private Long assignedToUserId;
}