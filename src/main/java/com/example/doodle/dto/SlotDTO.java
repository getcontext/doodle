package com.example.doodle.dto;

import java.time.Instant;
import java.util.UUID;

public record SlotDTO(
    UUID id,
    UUID calendarId,
    Instant startTime,
    Instant endTime,
    int capacity,
    int reservedCount,
    String status
) {}
