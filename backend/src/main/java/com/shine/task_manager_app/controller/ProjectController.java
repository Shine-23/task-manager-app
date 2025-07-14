package com.shine.task_manager_app.controller;

import com.shine.task_manager_app.dto.ProjectDTO;
import com.shine.task_manager_app.dto.ProjectRequest;
import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.entity.Project;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.exception.ResourceNotFoundException;
import com.shine.task_manager_app.service.ProjectService;
import com.shine.task_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<?> createProject(@RequestBody ProjectRequest request, Principal principal) {
        User owner = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedDate(LocalDateTime.now());
        project.setOwner(owner);

        // Fetch and assign users
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            List<User> members = userService.findUsersByIds(request.getMemberIds());
            project.setMembers(members);
        }

        Project saved = projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get projects of logged in user only
    @GetMapping
    public ResponseEntity<Page<Project>> getMyProjects(Pageable pageable, Principal principal) {
        User owner = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Page<Project> projects = projectService.findByOwner(owner, pageable);
        return ResponseEntity.ok(projects);
    }

    // Optional: Update and delete with ownership check
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDetails, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this project");
        }

        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());

        if (projectDetails.getMembersId() != null) {
            List<User> members = userService.findUsersByIds(projectDetails.getMembersId());
            project.setMembers(members); // ✅ apply update
        }


        projectService.saveProject(project);


        return ResponseEntity.ok(project);
    }

    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this project");
        }

        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted");
    }

    // GET /projects/owner/{ownerId} - projects owned by user (project manager)
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Page<Project>> getProjectsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return userService.findById(ownerId)
                .map(owner -> {
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Project> projects = projectService.findByOwner(owner, pageable);
                    return ResponseEntity.ok(projects);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /projects/assigned-to/{userId} - projects where user has assigned tasks
    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<Page<Project>> getProjectsByAssignedUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectService.findProjectsByUserAssignedTasks(userId, pageable);
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/{projectId}/assigned-users")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or @projectSecurity.isMember(#projectId, authentication)")
    public Page<UserDTO> getAssignedUsers(@PathVariable Long projectId, Pageable pageable) {
        return projectService.getUsersInProject(projectId, pageable);
    }

}
