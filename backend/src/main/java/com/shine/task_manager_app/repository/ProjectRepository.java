package com.shine.task_manager_app.repository;

import com.shine.task_manager_app.entity.Project;
import com.shine.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByOwner(User owner, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT t.project) FROM Task t WHERE t.assignedUser = :user")
    long countByOwner(User owner);

    @Query("SELECT COUNT(DISTINCT t.project) FROM Task t WHERE t.assignedUser = :user")
    long countProjectsByUserTasks(@Param("user") User user);

    @Query("SELECT DISTINCT p FROM Project p JOIN p.tasks t WHERE t.assignedUser.id = :userId")
    Page<Project> findProjectsByUserAssignedTasks(@Param("userId") Long userId, Pageable pageable);



}
