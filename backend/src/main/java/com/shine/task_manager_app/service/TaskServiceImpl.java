package com.shine.task_manager_app.service;

import com.shine.task_manager_app.entity.Project;
import com.shine.task_manager_app.entity.Task;
import com.shine.task_manager_app.entity.TaskStatus;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task saveTask(Task task) {return taskRepository.save(task); }

    @Override
    public Page<Task> findByProject(Project project, Pageable pageable) {return taskRepository.findByProject(project,pageable); }

    @Override
    public Page<Task> findByAssignedUser(User user, Pageable pageable) {return taskRepository.findByAssignedUser(user, pageable); }

    @Override
    public Page<Task> findByStatus(TaskStatus status, Pageable pageable) {return taskRepository.findByStatus(status, pageable);}

    @Override
    public Page<Task> findByDueDateBefore(LocalDate date, Pageable pageable) {return taskRepository.findByDueDateBefore(date, pageable); }

    @Override
    public Optional<Task> findTaskById(Long id) {return taskRepository.findById(id); }

    @Override
    public Optional<Task> updateTask(Task updatedTask) {
        return taskRepository.findById(updatedTask.getId()).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setAssignedUser(updatedTask.getAssignedUser());
            task.setProject(updatedTask.getProject());
            task.setDueDate(updatedTask.getDueDate());
            task.setStatus(updatedTask.getStatus());

            return taskRepository.save(task);
        });
    }

    @Override
    public void deleteTaskById(Long id) {taskRepository.deleteById(id); }

    @Override
    public Page<Task> findTasksByProjectOwner(User owner, Pageable pageable) {
        return taskRepository.findTasksByProjectOwner(owner, pageable);
    }

    @Override
    public long countTasksAssignedToUser(User user) {
        return taskRepository.countTasksByAssignedUser(user);
    }

    @Override
    public Page<Task> findByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId,pageable);
    }

    @Override
    public Page<Task> findByProjectIdAndAssignedTo(Long projectId, Long userId, Pageable pageable) {
        return taskRepository.findByProjectIdAndAssignedUserId(projectId, userId, pageable);
    }
}
