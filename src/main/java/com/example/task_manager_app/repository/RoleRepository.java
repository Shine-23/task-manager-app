package com.example.task_manager_app.repository;

import com.example.task_manager_app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName (String name); // Used to fetch roles by name like "ROLE_USER" or "ROLE_ADMIN"
}
