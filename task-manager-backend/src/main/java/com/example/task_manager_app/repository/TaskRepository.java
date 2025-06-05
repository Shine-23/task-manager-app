package com.example.task_manager_app.repository;

import com.example.task_manager_app.entity.Project;
import com.example.task_manager_app.entity.Task;
import com.example.task_manager_app.entity.TaskStatus;
import com.example.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProject(Project project, Pageable pageable);
    Page<Task> findByAssignedUser(User user, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByDueDateBefore(LocalDate date, Pageable pageable);
}
