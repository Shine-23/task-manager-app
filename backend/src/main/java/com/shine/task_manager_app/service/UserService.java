package com.shine.task_manager_app.service;

import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.dto.UserWithProjectDTO;
import com.shine.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    Page<UserDTO> getAllUsers(Pageable pageable);
    Optional<User> updateUser(Long id, User user);
    void deleteUser(Long id);

    Page<UserDTO> getUsersByRole(Pageable pageable);

    Page<UserDTO> getUnassignedUsers(Pageable pageable);

    List<User> findUsersByIds(List<Long> ids);

    Page<UserWithProjectDTO> getUsersWithProjectName(Pageable pageable);
}
