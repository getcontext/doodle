package com.example.doodle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ValidMeetingRequest
public record MeetingCreateRequest(
        @NotBlank String title,
        UUID calendarId,
        @NotNull UUID organizerId,
        UUID slotId,
        Instant startTime,
        Instant endTime,
        List<String> participantEmails
) {}
