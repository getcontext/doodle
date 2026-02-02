package com.example.doodle.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MeetingDTO(
    UUID id,
    UUID calendarId,
    String title,
    UUID organizerId,
    Instant startTime,
    Instant endTime,
    String status,
    List<String> participantEmails
) {}
