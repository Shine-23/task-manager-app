package com.shine.task_manager_app.service;

import com.shine.task_manager_app.dto.UserDTO;
import com.shine.task_manager_app.entity.Project;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.repository.ProjectRepository;
import com.shine.task_manager_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Project saveProject(Project project) {return projectRepository.save(project); }

    @Override
    public Page<Project> findByOwner(User owner, Pageable pageable) { return projectRepository.findByOwner(owner, pageable); }

    @Override
    public Optional<Project> getProjectById(Long id) { return projectRepository.findById(id); }

    @Override
    public Optional<Project> updateProject(Project updatedProject) {
        return projectRepository.findById(updatedProject.getId()).map(project ->{
                project.setName(updatedProject.getName());
                project.setDescription(updatedProject.getDescription());
                project.setOwner(updatedProject.getOwner());
                project.setTasks(updatedProject.getTasks());
                project.setCreatedDate(updatedProject.getCreatedDate());
                return projectRepository.save(project);
        });
    }

    @Override
    public void deleteProject(Long id) {projectRepository.deleteById(id); }

    @Override
    public long countByOwner(User owner) {
        return projectRepository.countByOwner(owner);
    }

    @Override
    public long countProjectsByUserTasks(User user) {
        return projectRepository.countProjectsByUserTasks(user);
    }

    @Override
    public Page<Project> findProjectsByUserAssignedTasks(Long userId, Pageable pageable) {
        return projectRepository.findProjectsByUserAssignedTasks(userId,pageable);
    }

    @Override
    public Page<UserDTO> getUsersInProject(Long projectId, Pageable pageable) {
        return userRepository.findUsersByProjectId(projectId, pageable);
    }

}
