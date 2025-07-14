package com.shine.task_manager_app.repository;

import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT new com.shine.task_manager_app.dto.UserDTO(u.id, u.username) " +
            "FROM User u JOIN u.projectsWorking p WHERE p.id = :projectId")
    Page<UserDTO> findUsersByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT new com.shine.task_manager_app.dto.UserDTO(u.id, u.username) FROM User u")
    Page<UserDTO> findAllUsers(Pageable pageable);

    @Query("SELECT new com.shine.task_manager_app.dto.UserDTO(u.id, u.username, u.email) " +
            "FROM User u JOIN u.roles r " +
            "WHERE r.name = 'ROLE_USER'")
    Page<UserDTO> findUsersByRole(Pageable pageable);


    @Query("SELECT new com.shine.task_manager_app.dto.UserDTO(u.id, u.username) " +
            "FROM User u " +
            "LEFT JOIN u.projectsWorking p " +
            "WHERE p IS NULL")
    Page<UserDTO> findUsersWithoutProject(Pageable pageable);

    @Query(value = "SELECT u.id AS id, u.username AS username, " +
            "(SELECT p.name FROM projects p " +
            "JOIN project_members pm ON p.id = pm.project_id " +
            "WHERE pm.user_id = u.id LIMIT 1) AS projectName " +
            "FROM users u",
            countQuery = "SELECT COUNT(*) FROM users",
            nativeQuery = true)
    Page<Object[]> findUsersWithProjectNameNative(Pageable pageable);

}
