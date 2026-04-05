package com.abdelrahman.backend.service;

import com.abdelrahman.backend.dto.task.TaskCreateRequest;
import com.abdelrahman.backend.dto.task.TaskResponse;
import com.abdelrahman.backend.dto.task.TaskStatusUpdateRequest;
import com.abdelrahman.backend.dto.task.TaskUpdateRequest;
import com.abdelrahman.backend.entity.Project;
import com.abdelrahman.backend.entity.Task;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.exception.BadRequestException;
import com.abdelrahman.backend.exception.ResourceNotFoundException;
import com.abdelrahman.backend.repository.ProjectMemberRepository;
import com.abdelrahman.backend.repository.TaskRepository;
import com.abdelrahman.backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectMemberRepository projectMemberRepository;

    public TaskResponse createTask(TaskCreateRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        Project project = projectService.getProjectEntityById(request.getProjectId());

        if (!projectMemberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new BadRequestException("Current user is not assigned to this project");
        }

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userService.getUserEntityById(request.getAssignedToUserId());

            if (!projectMemberRepository.existsByProjectAndUser(project, assignedTo)) {
                throw new BadRequestException("Assigned user is not a member of this project");
            }
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .project(project)
                .createdBy(currentUser)
                .assignedTo(assignedTo)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    public List<TaskResponse> getTasksByProject(Long projectId) {
        User currentUser = SecurityUtils.getCurrentUser();
        Project project = projectService.getProjectEntityById(projectId);

        if (!projectMemberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new BadRequestException("Current user is not allowed to view tasks of this project");
        }

        return taskRepository.findByProject(project)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = getTaskEntityById(taskId);
        Project project = task.getProject();
        User currentUser = SecurityUtils.getCurrentUser();

        if (!projectMemberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new BadRequestException("Current user is not allowed to update this task");
        }

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userService.getUserEntityById(request.getAssignedToUserId());

            if (!projectMemberRepository.existsByProjectAndUser(project, assignedTo)) {
                throw new BadRequestException("Assigned user is not a member of this project");
            }
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
        Project project = task.getProject();
        User currentUser = SecurityUtils.getCurrentUser();

        if (!projectMemberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new BadRequestException("Current user is not allowed to update task status");
        }

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