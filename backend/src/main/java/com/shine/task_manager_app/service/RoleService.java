package com.shine.task_manager_app.service;

import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(ERole name);
}
