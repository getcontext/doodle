Mini Doodle - Scheduling Service

This repository contains a minimal scheduling service ("mini Doodle") built with Spring Boot and PostgreSQL.

Quick start (requires Docker and Docker Compose):

1. Build and run the application and database:

```powershell
docker-compose up --build
```

2. API will be available at http://localhost:8080

Health: http://localhost:8080/actuator/health
Metrics (Prometheus): http://localhost:8080/actuator/prometheus

Basic usage:
- Create calendars, slots, and meetings via REST endpoints under /api/v1

See `src/main/resources/db/migration/V1__init.sql` for schema details.

Development:
- Build with Maven: mvn clean package
- Run locally (requires Java 17): mvn spring-boot:run

Notes:
- This is a minimal implementation for demonstration. See code comments for design decisions and TODOs.
