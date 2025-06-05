package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.ERole;
import com.example.task_manager_app.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(ERole name);
}
