package com.shine.task_manager_app.service;

import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.dto.UserWithProjectDTO;
import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {return userRepository.findAllUsers(pageable);}

    public Optional<User> findById(Long id){return userRepository.findById(id); }

    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            return userRepository.save(user);
        });
    }

    public void deleteUser(Long id){userRepository.deleteById(id); }

    public Page<UserDTO> getUsersByRole(Pageable pageable) {
        return userRepository.findUsersByRole(pageable);
    }

    @Override
    public Page<UserDTO> getUnassignedUsers(Pageable pageable) {
        return userRepository.findUsersWithoutProject(pageable);
    }

    @Override
    public List<User> findUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public Page<UserWithProjectDTO> getUsersWithProjectName(Pageable pageable) {
        Page<Object[]> result = userRepository.findUsersWithProjectNameNative(pageable);
        return result.map(obj -> new UserWithProjectDTO(
                ((Number) obj[0]).longValue(),
                (String) obj[1],
                (String) obj[2]
        ));
    }


}
