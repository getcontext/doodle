package com.example.doodle.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    private UUID id;

    @Column(name = "calendar_id")
    private UUID calendarId;

    private String title;

    @Column(name = "organizer_id")
    private UUID organizerId;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    private String status = "SCHEDULED";
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Meeting() { this.id = UUID.randomUUID(); }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCalendarId() { return calendarId; }
    public void setCalendarId(UUID calendarId) { this.calendarId = calendarId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public UUID getOrganizerId() { return organizerId; }
    public void setOrganizerId(UUID organizerId) { this.organizerId = organizerId; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
