package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface    UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> getUserById(Long id);
    Page<User> getAllUsers(Pageable pageable);
    Optional<User> updateUser(Long id, User user);
    void deleteUser(Long id);
}
