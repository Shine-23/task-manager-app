# Task Manager - Backend

A role-based task management REST API built with **Spring Boot** and **MySQL**. It supports secure user authentication, project and task management, and access control based on user roles.

## Features

- JWT-based Authentication (Login/Register)
- Role-based Authorization (`ADMIN`, `PROJECT_MANAGER`, `USER`)
- Project and Task CRUD operations
- Task assignment and due dates
- Project-to-user relationships
- Pagination and DTO-based responses
- Clean separation of layers (Controller, Service, Repository, DTOs)
- Secure password hashing with BCrypt

## Technologies Used

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven

