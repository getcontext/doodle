package com.example.doodle.dto;

import java.time.Instant;
import java.util.UUID;

public class SlotDTO {
    public UUID id;
    public UUID calendarId;
    public Instant startTime;
    public Instant endTime;
    public int capacity;
    public int reservedCount;
    public String status;
}
