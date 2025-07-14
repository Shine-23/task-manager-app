package com.shine.task_manager_app.service;

import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.entity.Project;
import com.shine.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjectService {
    Project saveProject(Project project);
    Page<Project> findByOwner(User owner, Pageable pageable);
    Optional<Project> getProjectById(Long id);
    Optional<Project> updateProject(Project project);
    void deleteProject(Long id);

    long countByOwner(User user);

    long countProjectsByUserTasks(User user);

    Page<Project> findProjectsByUserAssignedTasks(Long userId, Pageable pageable);

    Page<UserDTO> getUsersInProject(Long projectId, Pageable pageable);
}
