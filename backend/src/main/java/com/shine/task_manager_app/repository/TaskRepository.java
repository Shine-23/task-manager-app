package com.shine.task_manager_app.repository;

import com.shine.task_manager_app.entity.Project;
import com.shine.task_manager_app.entity.Task;
import com.shine.task_manager_app.entity.TaskStatus;
import com.shine.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.project.owner = :owner")
    Page<Task> findTasksByProjectOwner(@Param("owner") User owner, Pageable pageable);

    Page<Task> findByProject(Project project, Pageable pageable);

    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByAssignedUser(User user, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByDueDateBefore(LocalDate date, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.assignedUser.id = :userId")
    Page<Task> findByProjectIdAndAssignedUserId(@Param("projectId") Long projectId,
                                                @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM Task t WHERE t.assignedUser = :user")
    long countTasksByAssignedUser(@Param("user") User user);
}
