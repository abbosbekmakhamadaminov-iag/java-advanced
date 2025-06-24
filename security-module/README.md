# Security Module - Homework Tasks

This module implements 4 progressive security tasks using Spring Profiles for easy task switching and testing.

## Task Overview

| Task | Points | Description | Features |
|------|--------|-------------|----------|
| **Task 1** | 20 | Basic Security Setup | MVC project, REST endpoint, Basic auth |
| **Task 2** | 20 | Database & Password Security | Database users, Password hashing, Public endpoint |
| **Task 3** | 40 | Advanced Security | Role-based access, Custom login, Brute force protection |
| **Task 4** | 20 | Secret Sharing App | One-time secrets, Unique links, Docker |

## Running Tasks

### Task 1 - Basic Security Setup
```bash
# From project root directory
./gradlew :security-module:bootRun --args="--spring.profiles.active=task1"
```
- **Port**: 8084
- **Endpoints**: 
  - `GET /info` (authenticated) - Random stats
  - `GET /task-info` (public) - Task information
- **Auth**: user@example.com / password

### Task 2 - Database & Password Security  
```bash
./gradlew :security-module:bootRun --args="--spring.profiles.active=task2"
```
- **Features**: Database users, password hashing, public `/about` endpoint
- **H2 Console**: http://localhost:8084/h2-console

### Task 3 - Advanced Security
```bash
./gradlew :security-module:bootRun --args="--spring.profiles.active=task3"
```
- **Features**: Permission-based access, custom login/logout, brute force protection
- **Endpoints**: `/admin`, `/blocked-users`

### Task 4 - Secret Sharing Application
```bash
./gradlew :security-module:bootRun --args="--spring.profiles.active=task4"
```
- **Features**: One-time secret sharing with unique links
- **Docker**: See Dockerfile for containerization

## Testing Endpoints

### Task 1 Testing
```bash
# Check task info
curl http://localhost:8084/task-info

# Test authenticated endpoint
curl -u user@example.com:password http://localhost:8084/info
```

### Task 2 Testing
```bash
# Public endpoint
curl http://localhost:8084/about

# Authenticated endpoint with database users
curl -u user@example.com:password http://localhost:8084/info
curl -u admin@example.com:admin123 http://localhost:8084/info
```

### Task 3 Testing
```bash
# Permission-based endpoints
curl -u user1@example.com:password http://localhost:8084/info    # Has VIEW_INFO permission
curl -u user2@example.com:password http://localhost:8084/admin   # Has VIEW_ADMIN permission
curl -u admin@example.com:admin123 http://localhost:8084/admin   # Has both permissions

# Check blocked users (admin only)
curl -u admin@example.com:admin123 http://localhost:8084/blocked-users

# Test brute force protection (try wrong password 3 times)
curl -u user1@example.com:wrongpassword http://localhost:8084/info
```

### Task 4 Testing
```bash
# Create a secret (requires STANDARD permission)
curl -X POST http://localhost:8084/secret \
  -H "Content-Type: application/json" \
  -u sender@example.com:password \
  -d '{"content":"This is a secret message"}'

# Access the secret using the returned link (one-time only)
curl -u recipient@example.com:password http://localhost:8084/secret/{uniqueLink}

# Cleanup expired secrets (admin only)
curl -X POST -u admin@example.com:admin123 http://localhost:8084/cleanup
```

### Docker Testing (Task 4)
```bash
# Build and run with Docker
cd security-module
docker build -t security-task4 .
docker run -p 8084:8084 security-task4

# Or use docker-compose
docker-compose up
```

## Project Structure

```
security-module/
├── src/main/java/com/epam/javadvanced/security/
│   ├── SecurityApplication.java        # Main application
│   ├── task1/                         # Basic security components
│   ├── task2/                         # Database + hashing components  
│   ├── task3/                         # Advanced security components
│   ├── task4/                         # Secret provider components
│   └── shared/                        # Common utilities
├── src/main/resources/
│   ├── application.yml                # Default configuration
│   ├── application-task1.yml          # Task 1 config
│   ├── application-task2.yml          # Task 2 config
│   ├── application-task3.yml          # Task 3 config
│   └── application-task4.yml          # Task 4 config
└── README.md                          # This file
```

## Development Workflow

1. **Implement Task 1**: Basic MVC + Security
2. **Implement Task 2**: Add database persistence and password hashing
3. **Implement Task 3**: Add advanced security features
4. **Implement Task 4**: Create secret sharing functionality

Each task builds upon the previous one while being independently runnable and testable.