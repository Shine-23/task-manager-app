package com.example.task_manager_app.controller;

import com.example.task_manager_app.entity.*;
import com.example.task_manager_app.service.TaskService;
import com.example.task_manager_app.service.UserService;
import com.example.task_manager_app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    // Create new task
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Task saved = taskService.saveTask(task);
        return ResponseEntity.ok(saved);
    }

    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return taskService.updateTask(task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    // List tasks by project ID
    @GetMapping("/project/{projectId}")
    public Optional<ResponseEntity<Page<Task>>> getTasksByProject(@PathVariable Long projectId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Optional<Project> project = projectService.getProjectById(projectId);
        return project.map(p -> ResponseEntity.ok(taskService.findByProject(p, PageRequest.of(page, size))));
    }

    // List tasks by assigned user ID
    @GetMapping("/assigned/{userId}")
    public Optional<ResponseEntity<Page<Task>>> getTasksByUser(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(u -> ResponseEntity.ok(taskService.findByAssignedUser(u, PageRequest.of(page, size))));
    }

    // List tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable TaskStatus status,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.findByStatus(status, PageRequest.of(page, size)));
    }

    // List tasks due before date
    @GetMapping("/due-before/{date}")
    public ResponseEntity<?> getTasksDueBefore(@PathVariable String date,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        LocalDate dueDate = LocalDate.parse(date);
        return ResponseEntity.ok(taskService.findByDueDateBefore(dueDate, PageRequest.of(page, size)));
    }
}
