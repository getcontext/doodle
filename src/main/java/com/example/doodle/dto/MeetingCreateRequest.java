package com.example.doodle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ValidMeetingRequest
public class MeetingCreateRequest {
    @NotBlank
    public String title;
    public UUID calendarId;
    @NotNull
    public UUID organizerId;
    public UUID slotId; // optional
    public Instant startTime; // optional if slotId provided
    public Instant endTime;   // optional if slotId provided
    public List<String> participantEmails;
}
