package com.shine.task_manager_app.dto;

public class UserWithProjectDTO {
    private Long id;
    private String username;
    private String projectName;

    public UserWithProjectDTO(Long id, String username, String projectName) {
        this.id = id;
        this.username = username;
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

