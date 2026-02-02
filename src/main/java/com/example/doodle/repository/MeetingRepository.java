package com.example.doodle.repository;

import com.example.doodle.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    List<Meeting> findByCalendarIdAndStartTimeBetween(UUID calendarId, Instant from, Instant to);

    // find meetings that overlap given period
    List<Meeting> findByCalendarIdAndStartTimeLessThanAndEndTimeGreaterThan(UUID calendarId, Instant end, Instant start);
}
