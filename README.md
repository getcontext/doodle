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

API examples (curl / PowerShell)

1) Create a calendar (PowerShell)

```powershell
$body = @{
  name = 'Team Calendar'
  ownerId = '11111111-1111-1111-1111-111111111111'
  defaultTimeZone = 'UTC'
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/v1/calendars' -ContentType 'application/json' -Body $body
```

(curl)

```bash
curl -X POST http://localhost:8080/api/v1/calendars \
  -H 'Content-Type: application/json' \
  -d '{"name":"Team Calendar","ownerId":"11111111-1111-1111-1111-111111111111","defaultTimeZone":"UTC"}'
```

Sample response (201 Created)

```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "name": "Team Calendar",
  "ownerId": "11111111-1111-1111-1111-111111111111",
  "defaultTimeZone": "UTC",
  "createdAt": "2026-02-02T12:00:00Z"
}
```

2) Create a slot for a calendar

PowerShell

```powershell
$slot = @{
  startTime = '2026-02-03T10:00:00Z'
  endTime   = '2026-02-03T11:00:00Z'
  capacity  = 2
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/calendars/$calendarId/slots" -ContentType 'application/json' -Body $slot
```

curl

```bash
curl -X POST "http://localhost:8080/api/v1/calendars/<CALENDAR_ID>/slots" \
  -H 'Content-Type: application/json' \
  -d '{"startTime":"2026-02-03T10:00:00Z","endTime":"2026-02-03T11:00:00Z","capacity":2}'
```

Sample response (201 Created)

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "calendarId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "startTime": "2026-02-03T10:00:00Z",
  "endTime": "2026-02-03T11:00:00Z",
  "capacity": 2,
  "reservedCount": 0,
  "status": "AVAILABLE"
}
```

3) List slots (curl)

```bash
curl "http://localhost:8080/api/v1/calendars/<CALENDAR_ID>/slots?from=2026-02-01T00:00:00Z&to=2026-03-01T00:00:00Z"
```

4) Create meeting by slotId (PowerShell)

```powershell
$meeting = @{
  title = 'Sync'
  slotId = '3fa85f64-5717-4562-b3fc-2c963f66afa6'
  organizerId = '11111111-1111-1111-1111-111111111111'
  participantEmails = @('a@example.com','b@example.com')
} | ConvertTo-Json -Depth 4

Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/v1/meetings' -ContentType 'application/json' -Body $meeting
```

curl

```bash
curl -X POST http://localhost:8080/api/v1/meetings \
  -H 'Content-Type: application/json' \
  -d '{"title":"Sync","slotId":"3fa85f64-5717-4562-b3fc-2c963f66afa6","organizerId":"11111111-1111-1111-1111-111111111111","participantEmails":["a@example.com"]}'
```

Sample response (201 Created)

```json
{
  "id": "c0a8012e-0000-0000-0000-000000000000",
  "calendarId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "title": "Sync",
  "organizerId": "11111111-1111-1111-1111-111111111111",
  "startTime": "2026-02-03T10:00:00Z",
  "endTime": "2026-02-03T11:00:00Z",
  "status": "SCHEDULED",
  "participantEmails": ["a@example.com","b@example.com"]
}
```

5) Create meeting by start/end (no slot)

```bash
curl -X POST http://localhost:8080/api/v1/meetings \
  -H 'Content-Type: application/json' \
  -d '{"title":"Planning","calendarId":"f47ac10b-58cc-4372-a567-0e02b2c3d479","organizerId":"11111111-1111-1111-1111-111111111111","startTime":"2026-02-05T09:00:00Z","endTime":"2026-02-05T10:00:00Z","participantEmails":["x@example.com"]}'
```

6) Read endpoints

- Get calendar: GET /api/v1/calendars/{calendarId}
- Get meeting:  GET /api/v1/meetings/{meetingId}
- List meetings: GET /api/v1/meetings?calendarId=<CALENDAR_ID>&from=<ISO>&to=<ISO>

Errors and validation

- Validation errors (missing required fields, invalid times, etc.) return 400 Bad Request with JSON details:

```json
{ "error": "Validation failed", "details": [ "field: message", ... ] }
```

- Business conflicts (e.g., slot full, meeting overlaps) return 409 Conflict with a JSON error message:

```json
{ "error": "Slot is full or not available" }
```

Notes

- The API accepts and returns ISO-8601 instants (UTC), e.g. "2026-02-03T10:00:00Z".
- Calendar and meeting IDs are UUIDs.
- For more details about the DB schema and constraints see `src/main/resources/db/migration/V1__init.sql`.
