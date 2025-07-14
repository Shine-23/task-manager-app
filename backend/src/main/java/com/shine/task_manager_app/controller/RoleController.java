package com.shine.task_manager_app.controller;

import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.Role;
import com.shine.task_manager_app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Get role by name (enum value)
    @GetMapping("/{name}")
    public ResponseEntity<?> getRoleByName(@PathVariable String name) {
        try {
            ERole roleEnum = ERole.valueOf(name.toUpperCase());
            Optional<Role> role = roleService.findByName(roleEnum);

            if (role.isPresent()) {
                return ResponseEntity.ok(role.get());
            } else {
                return ResponseEntity.status(404).body("Role not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role name");
        }
    }
}
