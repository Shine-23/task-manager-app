# Task Manager Backend

A role-based task management REST API built with **Spring Boot** and **MySQL**. It supports secure user authentication, project and task management, and access control based on user roles.

---

## Features

- JWT-based Authentication (Login/Register)
- Role-based Authorization (`ADMIN`, `PROJECT_MANAGER`, `USER`)
- Project and Task CRUD operations
- Task assignment and due dates
- Project-to-user relationships
- Pagination and DTO-based responses
- Clean separation of layers (Controller, Service, Repository, DTOs)
- Secure password hashing with BCrypt

---



## Entity Relationships

- `User` ⟷ `Role` → `@ManyToMany`
- `User` ⟷ `Project` → `@ManyToMany`
- `Project` ⟶ `Task` → `@OneToMany`
- `User` ⟶ `Task` → `@OneToMany` (as assignee)

---

##  Role-based Access

| Role            | Access To                                                                 |
|------------------|--------------------------------------------------------------------------|
| `ADMIN`          | View all users                                                           |
| `PROJECT_MANAGER`| Manage own projects, all tasks in owned projects                         |
| `USER`           | View/update only their assigned tasks in a project they are part of      |

---

## Technologies Used

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven

---

## How to Run

### 1. Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

jwt.secret=your_jwt_secret_key
jwt.expiration=3600000
```

### 2. Build & Run
```aiignore
mvn clean install
mvn spring-boot:run
```
 
### 3. Access
- API base URL: `http://localhost:8080`
- Use JWT in header for protected routes:`Authorization: Bearer <your_token>`

##  Testing the API
You can test endpoints using tools like:
- Postman (Import as a collection)
- cURL
- Or connect to the React frontend

## Folder Structure

```
src/main/java/com/example/task_manager_app
├── config/          # Security and JWT configs
├── controller/      # REST Controllers
├── dto/             # Request/response DTOs
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── repository/      # Spring Data Repositories
├── security/        # JWT utility and filters
├── service/         # Service interfaces and implementations
└── TaskManagerApp.java
```
##  JWT Flow
1. Register at /auth/register
2. Login at /auth/login → Receive JWT token
3. Use token for all protected endpoints with: `Authorization: Bearer <token>`

---
## Notes
- All tasks and projects are scoped per user access and ownership.
- Proper error handling using @ControllerAdvice
- DTOs are used to avoid exposing sensitive fields like passwords.