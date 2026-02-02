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


Endpoints & examples

Create calendar
POST /api/v1/calendars
Request JSON { "name": "Team Calendar", "ownerId": "11111111-1111-1111-1111-111111111111", "defaultTimeZone": "UTC" }
Returns 201 with the saved Calendar entity (JSON representation of the JPA entity).

Create slot for a calendar
POST /api/v1/calendars/{calendarId}/slots
Request JSON (CreateSlotRequest already exists) { "startTime": "2026-02-03T10:00:00Z", "endTime": "2026-02-03T11:00:00Z", "capacity": 2 }
Returns 201 with SlotDTO: { "id": "...", "calendarId": "...", "startTime":"2026-02-03T10:00:00Z", "endTime":"2026-02-03T11:00:00Z", "capacity": 2, "reservedCount": 0, "status": "AVAILABLE" }

List slots
GET /api/v1/calendars/{calendarId}/slots?from=2026-02-01T00:00:00Z&to=2026-03-01T00:00:00Z
from/to are optional ISO-8601 instant strings. If omitted, from=Instant.EPOCH, to=now+1year.
Returns List<slotdto>.</slotdto>

Create meeting
POST /api/v1/meetings
Two ways to schedule:
By slotId (server will attempt to reserve the slot atomically and create the meeting): { "title": "Sync", "slotId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "organizerId": "11111111-1111-1111-1111-111111111111", "participantEmails": ["a@example.com","b@example.com"] }
By start/end + calendarId (no slot): { "title": "Sync", "calendarId": "22222222-2222-2222-2222-222222222222", "organizerId": "11111111-1111-1111-1111-111111111111", "startTime":"2026-02-03T10:00:00Z", "endTime":"2026-02-03T11:00:00Z", "participantEmails": ["a@example.com"] }
Returns 201 with MeetingDTO including participantEmails.