package com.example.task_manager_app.controller;

import com.example.task_manager_app.entity.*;
import com.example.task_manager_app.exception.ResourceNotFoundException;
import com.example.task_manager_app.repository.UserRepository;
import com.example.task_manager_app.service.TaskService;
import com.example.task_manager_app.service.UserService;
import com.example.task_manager_app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Create task only if current user owns the project
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = projectService.getProjectById(task.getProject().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this project");
        }

        // Optional: Validate assigned user exists
        if (task.getAssignedUser() != null) {
            userService.findById(task.getAssignedUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
        }

        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<Page<Task>> getTasksAssignedToUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return userRepository.findById(userId)
                .map(user -> {
                    Page<Task> tasks = taskService.findByAssignedUser(user, PageRequest.of(page, size));
                    return ResponseEntity.ok(tasks);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // Get tasks in projects owned by current user
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    @GetMapping("/my-projects")
    public ResponseEntity<Page<Task>> getTasksInMyProjects(Pageable pageable, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // This method you may need to implement in TaskRepository to fetch tasks by project owners
        Page<Task> tasks = taskService.findTasksByProjectOwner(currentUser, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/by-project/{projectId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public Page<Task> getTasksByProject(@PathVariable Long projectId, Pageable pageable) {
        return taskService.findByProjectId(projectId, pageable);
    }

    @GetMapping("/by-project-and-user/{projectId}/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PROJECT_MANAGER')")
    public Page<Task> getTasksByProjectAndUser(@PathVariable Long projectId, @PathVariable Long userId, Pageable pageable) {
        return taskService.findByProjectIdAndAssignedTo(projectId, userId, pageable);
    }
}
