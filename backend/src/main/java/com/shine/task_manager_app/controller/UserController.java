package com.shine.task_manager_app.controller;


import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.dto.UserWithProjectDTO;
import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.shine.task_manager_app.entity.ERole.ROLE_USER;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/ROLE_USER")
    public Page<UserDTO> getUsersWithRoleUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUsersByRole(pageable);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @GetMapping("/unassigned")
    public Page<UserDTO> getUnassignedUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUnassignedUsers(pageable);
    }


    // Get user by ID (Admin or Self)
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @GetMapping("{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    // Update user (email or password) - Admin or Self
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> updated = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(updated);
    }

    // Delete user (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @GetMapping("/with-projects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Page<UserWithProjectDTO> getUsersWithProjects(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUsersWithProjectName(pageable);
    }

}
