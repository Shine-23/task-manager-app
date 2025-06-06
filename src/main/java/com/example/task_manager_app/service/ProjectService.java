package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.Project;
import com.example.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjectService {
    Project saveProject(Project project);
    Page<Project> findByOwner(User owner, Pageable pageable);
    Optional<Project> getProjectById(Long id);
    Optional<Project> updateProject(Project project);
    void deleteProject(Long id);
}
