package com.example.doodle.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    private UUID id;

    @Column(name = "meeting_id")
    private UUID meetingId;

    @Column(name = "user_id")
    private UUID userId;

    private String email;
    private String response = "UNKNOWN";
    private Instant respondedAt;

    public Participant() { this.id = UUID.randomUUID(); }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getMeetingId() { return meetingId; }
    public void setMeetingId(UUID meetingId) { this.meetingId = meetingId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public Instant getRespondedAt() { return respondedAt; }
    public void setRespondedAt(Instant respondedAt) { this.respondedAt = respondedAt; }
}
