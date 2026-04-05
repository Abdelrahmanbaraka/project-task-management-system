package com.abdelrahman.backend.service;

import com.abdelrahman.backend.dto.project.ProjectProgressResponse;
import com.abdelrahman.backend.entity.Project;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.enums.TaskStatus;
import com.abdelrahman.backend.repository.ProjectMemberRepository;
import com.abdelrahman.backend.repository.ProjectRepository;
import com.abdelrahman.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Test
    void shouldCalculateProjectProgressCorrectly() {
        ProjectRepository projectRepository = mock(ProjectRepository.class);
        ProjectMemberRepository projectMemberRepository = mock(ProjectMemberRepository.class);
        TaskRepository taskRepository = mock(TaskRepository.class);
        UserService userService = mock(UserService.class);

        ProjectService projectService = new ProjectService(
                projectRepository,
                projectMemberRepository,
                taskRepository,
                userService
        );

        Project project = Project.builder()
                .id(1L)
                .name("Test Project")
                .createdBy(User.builder().id(1L).username("leader").build())
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.countByProject(project)).thenReturn(4L);
        when(taskRepository.countByProjectAndStatus(project, TaskStatus.DONE)).thenReturn(2L);

        ProjectProgressResponse response = projectService.getProjectProgress(1L);

        assertEquals(1L, response.getProjectId());
        assertEquals("Test Project", response.getProjectName());
        assertEquals(4L, response.getTotalTasks());
        assertEquals(2L, response.getCompletedTasks());
        assertEquals(50.0, response.getProgressPercentage());
    }
}