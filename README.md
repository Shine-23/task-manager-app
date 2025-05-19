# Task Manager App

This is a Spring Boot-based Task Manager application with JWT-based authentication and role-based access control. The app allows users to manage tasks securely by authenticating with JSON Web Tokens (JWT).

## Project Structure
```
config
└── SecurityConfig               # Security configuration for JWT and Spring Security

controller
└── AuthController               # Controller for user authentication (login)

entity
├── Role                        # Entity representing user roles (e.g., ADMIN, USER)
├── Task                        # Entity representing a task
└── User                        # Entity representing an application user

repository
├── RoleRepository              # Data access layer for roles
├── TaskRepository              # Data access layer for tasks
└── UserRepository              # Data access layer for users

security
├── JwtAuthFilter               # JWT authentication filter for validating tokens
└── JwtTokenProvider            # Utility class for generating and validating JWT tokens

service
├── CustomUserDetailsService    # Loads user details for authentication
├── RoleService                 # Business logic related to roles
├── TaskService                 # Business logic related to tasks
└── UserService                 # Business logic related to users

```

## Features

- **User Authentication**
  - Login endpoint `/api/auth/login` for users to authenticate using username and password.
  - On successful login, a JWT token is generated and returned.
  - Uses Spring Security's `AuthenticationManager` for authentication.
  
- **JWT Security**
  - Token generation and validation via `JwtTokenProvider`.
  - Requests are filtered and authenticated by `JwtAuthFilter`.

- **Role-Based Access Control**
  - Roles are defined and managed in the system to secure endpoints based on user roles.

- **Task Management**
  - Users can create, update, and manage tasks securely (to be implemented in TaskController and TaskService).


## How to Use

1. Clone the repository.
2. Configure your database in `application.properties`.
3. Build and run the Spring Boot application.
4. Use the `/api/auth/login` endpoint to authenticate:

   ```bash
   POST /api/auth/login
   Content-Type: application/json

   {
     "username": "your_username",
     "password": "your_password"
   }
   ```
5. The response will include a JWT token:
  ```bash
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
```
6.   Use this token in the `Authorization header` (Bearer token) for accessing protected endpoints.

## Technologies Used
- Java 17+
- Spring Boot 3.x
- Spring Security 6.x
- JWT (JSON Web Tokens) for authentication
- Spring Data JPA with MySQL database
- Maven for build and dependency management

## Getting Started
### Prerequisites
- Java 17 or later
- Maven 3.6+
- MySQL 8+
- Postman or any REST client for testing APIs
### Setup
1. Clone the repository
```bash
git clone <repository-url>
```
2. Configure database settings in `application.properties`
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
4. Build and run the application
```bash
mvn clean install
mvn spring-boot:run
```
## API Endpoints
### Authentication
- `POST /api/auth/login` - Authenticate user and receive JWT token
### Tasks
- `GET /api/tasks` - Get all tasks (ROLE_USER or ROLE_ADMIN)
- `POST /api/tasks` - Create a new task (ROLE_USER or ROLE_ADMIN)
- `GET /api/tasks/{id}` - Get task by ID (ROLE_USER or ROLE_ADMIN)
- `PUT /api/tasks/{id}` - Update task by ID (ROLE_USER or ROLE_ADMIN)
- `DELETE /api/tasks/{id}` - Delete task by ID (ROLE_ADMIN only)

## Security
- JWT tokens are issued upon successful authentication.
- JWT tokens must be included in the  `Authorization` header as `Bearer <token>`.
- Custom JWT filter validates tokens on every request.
- Role-based method and endpoint security enforced by Spring Security.

## Contribution
Feel free to fork and create pull requests for any improvements or bug fixes <3.




