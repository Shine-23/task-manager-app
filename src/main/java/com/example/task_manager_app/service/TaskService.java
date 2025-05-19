package com.example.task_manager_app.service;

import com.example.task_manager_app.entity.Task;
import com.example.task_manager_app.entity.User;
import com.example.task_manager_app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Optional<Task> findTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Page<Task> findTaskByUser(User user, int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return taskRepository.findByUser(user, pageable);
    }

    public Task updateTask(Task updatedTask){
        Task existingTask = taskRepository.findById(updatedTask .getId())
                .orElseThrow(() -> new RuntimeException("Task not found with id:" + updatedTask.getId()));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setCreatedAt(updatedTask.getCreatedAt());
        existingTask.setUpdatedAt(updatedTask.getUpdatedAt());

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
