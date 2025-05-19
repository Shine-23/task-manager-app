package com.example.task_manager_app.repository;

import com.example.task_manager_app.entity.Task;
import com.example.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable); // Get tasks belonging to a specific user, Add pagination to the task list
}
