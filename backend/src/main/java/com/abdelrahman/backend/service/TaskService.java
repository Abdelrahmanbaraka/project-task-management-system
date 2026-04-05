package com.abdelrahman.backend.service;

import com.abdelrahman.backend.dto.task.TaskCreateRequest;
import com.abdelrahman.backend.dto.task.TaskResponse;
import com.abdelrahman.backend.dto.task.TaskStatusUpdateRequest;
import com.abdelrahman.backend.dto.task.TaskUpdateRequest;
import com.abdelrahman.backend.entity.Project;
import com.abdelrahman.backend.entity.Task;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.exception.ResourceNotFoundException;
import com.abdelrahman.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskResponse createTask(TaskCreateRequest request) {
        Project project = projectService.getProjectEntityById(request.getProjectId());
        User createdBy = userService.getUserEntityById(request.getCreatedByUserId());

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userService.getUserEntityById(request.getAssignedToUserId());
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .project(project)
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    public List<TaskResponse> getTasksByProject(Long projectId) {
        Project project = projectService.getProjectEntityById(projectId);

        return taskRepository.findByProject(project)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = getTaskEntityById(taskId);

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userService.getUserEntityById(request.getAssignedToUserId());
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAssignedTo(assignedTo);

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    public TaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request) {
        Task task = getTaskEntityById(taskId);
        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    public Task getTaskEntityById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .projectId(task.getProject().getId())
                .createdByUserId(task.getCreatedBy().getId())
                .createdByUsername(task.getCreatedBy().getUsername())
                .assignedToUserId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToUsername(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}