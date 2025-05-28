package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.ERole;
import com.example.task_manager_app.entity.Role;
import com.example.task_manager_app.entity.User;
import com.example.task_manager_app.repository.UserRepository;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, Set<String> roleNames){
        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        for(String roleName:roleNames){
            Role role = roleService.findByName(ERole.valueOf(roleName))
                    .orElseThrow( () -> new RuntimeException("Role not found: "+ roleName));
            roles.add(role);
        }

        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
