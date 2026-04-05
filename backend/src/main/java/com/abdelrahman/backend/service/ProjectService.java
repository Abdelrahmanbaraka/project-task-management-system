package com.abdelrahman.backend.service;

import com.abdelrahman.backend.dto.project.ProjectCreateRequest;
import com.abdelrahman.backend.dto.project.ProjectProgressResponse;
import com.abdelrahman.backend.dto.project.ProjectResponse;
import com.abdelrahman.backend.entity.Project;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.enums.TaskStatus;
import com.abdelrahman.backend.exception.ResourceNotFoundException;
import com.abdelrahman.backend.repository.ProjectRepository;
import com.abdelrahman.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public ProjectResponse createProject(ProjectCreateRequest request) {
        User createdBy = userService.getUserEntityById(request.getCreatedByUserId());

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .archived(false)
                .createdBy(createdBy)
                .build();

        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Project getProjectEntityById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public ProjectProgressResponse getProjectProgress(Long projectId) {
        Project project = getProjectEntityById(projectId);

        long totalTasks = taskRepository.countByProject(project);
        long completedTasks = taskRepository.countByProjectAndStatus(project, TaskStatus.DONE);

        double progress = totalTasks == 0 ? 0.0 : ((double) completedTasks / totalTasks) * 100;

        return ProjectProgressResponse.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .progressPercentage(progress)
                .build();
    }

    private ProjectResponse mapToResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .archived(project.getArchived())
                .createdByUserId(project.getCreatedBy().getId())
                .createdByUsername(project.getCreatedBy().getUsername())
                .build();
    }
}