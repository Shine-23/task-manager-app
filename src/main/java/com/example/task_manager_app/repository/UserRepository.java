package com.example.task_manager_app.repository;

import com.example.task_manager_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); // Used for login, signup checks, and fetching user info.
    boolean existsByUsername(String username);
}
