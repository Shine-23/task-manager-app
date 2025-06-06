package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.Project;
import com.example.task_manager_app.entity.Task;
import com.example.task_manager_app.entity.TaskStatus;
import com.example.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskService {
    Task saveTask(Task task);
    Page<Task> findByProject(Project project, Pageable pageable);
    Page<Task> findByAssignedUser(User user, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByDueDateBefore(LocalDate date, Pageable pageable);
    Optional<Task> findTaskById(Long id);
    Optional<Task> updateTask(Task task);
    void deleteTask(Long id);
}
