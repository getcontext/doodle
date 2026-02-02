package com.example.doodle.repository;

import com.example.doodle.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotRepository extends JpaRepository<Slot, UUID> {
    List<Slot> findByCalendarIdAndStartTimeBetween(UUID calendarId, Instant from, Instant to);

    @Modifying
    @Query("update Slot s set s.reservedCount = s.reservedCount + 1 where s.id = :id and s.reservedCount < s.capacity")
    int tryIncrementReservedCount(UUID id);

    Optional<Slot> findByIdAndCalendarId(UUID id, UUID calendarId);
}
