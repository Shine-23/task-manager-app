package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.Project;
import com.example.task_manager_app.entity.User;
import com.example.task_manager_app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

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
}
