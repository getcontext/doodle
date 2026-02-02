package com.example.doodle.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MeetingDTO {
    public UUID id;
    public UUID calendarId;
    public String title;
    public UUID organizerId;
    public Instant startTime;
    public Instant endTime;
    public String status;
    public List<String> participantEmails;
}
