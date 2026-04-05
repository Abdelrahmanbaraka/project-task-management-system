package com.abdelrahman.backend.repository;

import com.abdelrahman.backend.entity.Project;
import com.abdelrahman.backend.entity.Task;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByAssignedTo(User assignedTo);
    long countByProject(Project project);
    long countByProjectAndStatus(Project project, TaskStatus status);
}