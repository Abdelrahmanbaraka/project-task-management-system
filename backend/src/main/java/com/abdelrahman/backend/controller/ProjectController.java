package com.abdelrahman.backend.controller;

import com.abdelrahman.backend.dto.project.ProjectCreateRequest;
import com.abdelrahman.backend.dto.project.ProjectProgressResponse;
import com.abdelrahman.backend.dto.project.ProjectResponse;
import com.abdelrahman.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{projectId}/progress")
    public ProjectProgressResponse getProjectProgress(@PathVariable Long projectId) {
        return projectService.getProjectProgress(projectId);
    }
}