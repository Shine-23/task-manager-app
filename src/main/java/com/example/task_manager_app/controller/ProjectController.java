package com.example.task_manager_app.controller;

import com.example.task_manager_app.entity.Project;
import com.example.task_manager_app.entity.User;
import com.example.task_manager_app.service.ProjectService;
import com.example.task_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    // Create a new project
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        Project saved = projectService.saveProject(project);
        return ResponseEntity.ok(saved);
    }

    // Get project by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a project
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        return projectService.updateProject(project)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a project
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    // Get projects by user (owner)
    @GetMapping("/owner/{userId}")
    public Optional<ResponseEntity<Page<Project>>> getProjectsByOwner(@PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(u -> ResponseEntity.ok(projectService.findByOwner(u, PageRequest.of(page, size))));
    }
}
