package com.example.doodle.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "calendars")
public class Calendar {
    @Id
    private UUID id;

    private String name;

    @Column(name = "owner_id")
    private UUID ownerId;

    private String defaultTimeZone;
    private Instant createdAt = Instant.now();

    public Calendar() { this.id = UUID.randomUUID(); }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
    public String getDefaultTimeZone() { return defaultTimeZone; }
    public void setDefaultTimeZone(String defaultTimeZone) { this.defaultTimeZone = defaultTimeZone; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
