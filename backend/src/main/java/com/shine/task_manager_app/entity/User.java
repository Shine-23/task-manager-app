package com.shine.task_manager_app.entity;

//represents the person using the app

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;

@Entity

@Table(name = "users") // 'user' is a reserved keyword in MySQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @ManyToMany(fetch =FetchType.EAGER) //Load the relationship immediately along with the main entity.
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Project> projectsOwned = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "members")
    private List<Project> projectsWorking = new ArrayList<>();

    public User() {}

    public User(Long id, String username, String password, String email, Set<Role> roles, List<Project> projectsOwned, List<Project> projectsWorking) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.projectsOwned = projectsOwned;
        this.projectsWorking = projectsWorking;
    }

    public User(Long id, String username) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Project> getProjectsOwned() {return projectsOwned; }

    public void setProjectsOwned(List<Project> projectsOwned) {this.projectsOwned = projectsOwned;}

    public List<Project> getProjectsWorking() {
        return projectsWorking;
    }
    public void setProjectsWorking(List<Project> projectsWorking) {
        this.projectsWorking = projectsWorking;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, roles);
    }
}
