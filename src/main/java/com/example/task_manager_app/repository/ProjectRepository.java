package com.example.task_manager_app.repository;

import com.example.task_manager_app.entity.Project;
import com.example.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByOwner(User owner, Pageable pageable);
}
