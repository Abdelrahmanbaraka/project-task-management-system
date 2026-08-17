# Project Task Management System

A full-stack learning project for managing users, projects and tasks in a small IT-service organization. The application replaces an Excel-style workflow with structured data, role-based access and centralized project progress.

## Core functionality

- User management with `ADMIN`, `PROJECT_LEADER` and `EMPLOYEE` roles
- Project creation, editing and archiving
- Assignment of employees to projects
- Task creation and status management
- Project progress calculated from completed tasks
- Access restrictions based on role and project membership
- HTTP Basic authentication for the demonstration environment

## Technology stack

| Layer | Technology |
|---|---|
| Frontend | React 19, Vite, React Router |
| Backend | Java 21, Spring Boot 4, Spring MVC |
| Security | Spring Security |
| Persistence | JPA/Hibernate, PostgreSQL |
| Build and tests | Maven, Spring Boot Test |

## Repository structure

```text
project-task-management-system/
├── backend/    Spring Boot REST application
├── frontend/   React client
└── docs/       Architecture and project documentation
```

## Local setup

Requirements: Java 21, Node.js, npm and PostgreSQL.

```sql
CREATE DATABASE task_management_db;
```

Start the backend:

```bash
cd backend
./mvnw spring-boot:run
```

Start the frontend in a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Run backend tests with:

```bash
cd backend
./mvnw test
```

## Security scope

The project demonstrates authentication and role-based authorization. HTTP Basic authentication was selected to keep the learning project focused on project and task workflows; it should be replaced by a stronger production authentication design before real-world deployment.

## Current limitations

- No multi-tenant isolation
- Basic authentication rather than token- or session-based production authentication
- Simple user interface focused on functionality
- Local PostgreSQL configuration is required
- No public hosted demo
