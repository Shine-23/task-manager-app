package com.shine.task_manager_app.service;

import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.Role;
import com.shine.task_manager_app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(ERole name){
        return roleRepository.findByName(name);
    }
}
