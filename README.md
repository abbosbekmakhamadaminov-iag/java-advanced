# Java Advanced Learning Project

Multi-module Spring Boot project demonstrating traditional and reactive programming patterns.

## Project Structure

Based on learning modules and tasks:
- **Module 1 - Task 1**: Basic Spring Boot setup (Initial CommandLineRunner)
- **Module 1 - Task 2**: CRUD operations for Customer and Product entities  
- **Task 3**: Reactive programming with WebFlux and ETL processing

## Modules

- **crud-service**: Traditional Spring Boot CRUD API with JPA (Module 1 - Task 2)
- **etl-service**: Reactive Spring Boot ETL service with WebFlux (Task 3)
- **security-module**: Spring Security homework with 4 progressive tasks (Spring Boot 2.7.18)
- **shared-models**: Common entities shared across services

## Prerequisites

- Java 17+
- Gradle 8+

## Quick Start

### Build All Modules
```bash
./gradlew build
```

### Run CRUD Service (Module 1 - Task 2)
```bash
./gradlew :crud-service:bootRun
```
- **Port**: 8082
- **API**: http://localhost:8082/api/customers, http://localhost:8082/api/products
- **H2 Console**: http://localhost:8082/h2-console

### Run ETL Service (Task 3)
```bash
./gradlew :etl-service:bootRun
```
- **Port**: 8080
- **API**: http://localhost:8080/api/v1/sport
- **Features**: Reactive ETL, Rakuten API integration, backpressure control

### Run Security Module (Homework Tasks)
```bash
# Task 1 - Basic Security Setup
./gradlew :security-module:bootRun --args="--spring.profiles.active=task1"

# Task 2 - Database & Password Security  
./gradlew :security-module:bootRun --args="--spring.profiles.active=task2"

# Task 3 - Advanced Security
./gradlew :security-module:bootRun --args="--spring.profiles.active=task3"

# Task 4 - Secret Sharing App
./gradlew :security-module:bootRun --args="--spring.profiles.active=task4"
```
- **Port**: 8084
- **Features**: Progressive security tasks with Spring Boot 2.7.18

## API Examples

### CRUD Service
```bash
# Get all customers
curl http://localhost:8082/api/customers

# Create product
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"Gaming laptop","price":999.99,"stockQuantity":10}'
```

### ETL Service
```bash
# Create sport
curl -X POST http://localhost:8080/api/v1/sport/football

# Search sports
curl "http://localhost:8080/api/v1/sport?q=foot"
```

## Technology Stack

- **Backend**: Spring Boot 3.4.5, Spring WebFlux, Spring Data JPA/R2DBC
- **Database**: H2 (in-memory)
- **Build**: Gradle multi-module
- **Java**: 17