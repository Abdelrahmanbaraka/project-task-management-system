package com.abdelrahman.backend.controller;

import com.abdelrahman.backend.dto.task.TaskCreateRequest;
import com.abdelrahman.backend.dto.task.TaskResponse;
import com.abdelrahman.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getTasksByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId);
    }
}