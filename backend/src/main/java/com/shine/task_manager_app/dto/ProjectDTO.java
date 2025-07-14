package com.shine.task_manager_app.dto;

import java.util.List;

public class ProjectDTO {
    private String name;
    private String description;
    private List<Long> membersId;

    public List<Long> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<Long> membersId) {
        this.membersId = membersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

