package com.abdelrahman.backend.service;

import com.abdelrahman.backend.dto.project.ProjectCreateRequest;
import com.abdelrahman.backend.dto.project.ProjectMemberAssignRequest;
import com.abdelrahman.backend.dto.project.ProjectProgressResponse;
import com.abdelrahman.backend.dto.project.ProjectResponse;
import com.abdelrahman.backend.dto.project.ProjectUpdateRequest;
import com.abdelrahman.backend.entity.Project;
import com.abdelrahman.backend.entity.ProjectMember;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.enums.TaskStatus;
import com.abdelrahman.backend.exception.BadRequestException;
import com.abdelrahman.backend.exception.ResourceNotFoundException;
import com.abdelrahman.backend.repository.ProjectMemberRepository;
import com.abdelrahman.backend.repository.ProjectRepository;
import com.abdelrahman.backend.repository.TaskRepository;
import com.abdelrahman.backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public ProjectResponse createProject(ProjectCreateRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .archived(false)
                .createdBy(currentUser)
                .build();

        Project savedProject = projectRepository.save(project);

        if (!projectMemberRepository.existsByProjectAndUser(savedProject, currentUser)) {
            projectMemberRepository.save(
                    ProjectMember.builder()
                            .project(savedProject)
                            .user(currentUser)
                            .build()
            );
        }

        return mapToResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProjectResponse> getProjectsByCurrentUser() {
        User currentUser = SecurityUtils.getCurrentUser();

        return projectMemberRepository.findByUser(currentUser)
                .stream()
                .map(ProjectMember::getProject)
                .distinct()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProjectResponse> getProjectsByUser(Long userId) {
        User user = userService.getUserEntityById(userId);

        return projectMemberRepository.findByUser(user)
                .stream()
                .map(ProjectMember::getProject)
                .distinct()
                .map(this::mapToResponse)
                .toList();
    }

    public Project getProjectEntityById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request) {
        Project project = getProjectEntityById(projectId);

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        Project updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject);
    }

    public ProjectResponse archiveProject(Long projectId) {
        Project project = getProjectEntityById(projectId);
        project.setArchived(true);

        Project archivedProject = projectRepository.save(project);
        return mapToResponse(archivedProject);
    }

    public String assignMemberToProject(ProjectMemberAssignRequest request) {
        Project project = getProjectEntityById(request.getProjectId());
        User user = userService.getUserEntityById(request.getUserId());

        if (projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new BadRequestException("User is already assigned to this project");
        }

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .build();

        projectMemberRepository.save(projectMember);
        return "User assigned to project successfully";
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