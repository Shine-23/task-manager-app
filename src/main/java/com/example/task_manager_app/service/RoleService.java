package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.Role;
import com.example.task_manager_app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }
}
