package com.shine.task_manager_app.controller;

import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.service.ProjectService;
import com.shine.task_manager_app.service.TaskService;
import com.shine.task_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getDashboardStats(Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isProjectManager = user.getRoles().stream()
                .anyMatch(role -> role.getName() == ERole.ROLE_PROJECT_MANAGER);

        long projectCount;
        if (isProjectManager) {
            // Projects owned by user
            projectCount = projectService.countByOwner(user);
        } else {
            // Projects user has tasks in
            projectCount = projectService.countProjectsByUserTasks(user);
        }

        long taskCount = taskService.countTasksAssignedToUser(user);
        Map<String, Long> stats = Map.of(
                "projectCount", projectCount,
                "taskCount", taskCount
        );

        return ResponseEntity.ok(stats);
    }
}

