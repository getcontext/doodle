package com.example.doodle.controller;

import com.example.doodle.dto.CreateCalendarRequest;
import com.example.doodle.dto.CreateSlotRequest;
import com.example.doodle.dto.SlotDTO;
import com.example.doodle.model.Calendar;
import com.example.doodle.model.Slot;
import com.example.doodle.repository.CalendarRepository;
import com.example.doodle.service.SlotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class CalendarController {
    private final CalendarRepository calendarRepository;
    private final SlotService slotService;

    public CalendarController(CalendarRepository calendarRepository, SlotService slotService) {
        this.calendarRepository = calendarRepository;
        this.slotService = slotService;
    }

    @PostMapping("/calendars")
    public ResponseEntity<Calendar> createCalendar(@Valid @RequestBody CreateCalendarRequest req) {
        Calendar c = new Calendar();
        c.setName(req.name);
        c.setOwnerId(req.ownerId);
        c.setDefaultTimeZone(req.defaultTimeZone);
        Calendar saved = calendarRepository.save(c);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/calendars/{calendarId}")
    public ResponseEntity<Calendar> getCalendar(@PathVariable UUID calendarId) {
        Optional<Calendar> c = calendarRepository.findById(calendarId);
        return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/calendars/{calendarId}/slots")
    public ResponseEntity<SlotDTO> createSlot(@PathVariable UUID calendarId, @Valid @RequestBody CreateSlotRequest req) {
        Slot s = slotService.createSlot(calendarId, req);
        return ResponseEntity.status(201).body(toDto(s));
    }

    @GetMapping("/calendars/{calendarId}/slots")
    public List<SlotDTO> listSlots(@PathVariable UUID calendarId,
                                   @RequestParam(required = false) String from,
                                   @RequestParam(required = false) String to) {
        Instant f = (from != null) ? Instant.parse(from) : Instant.EPOCH;
        Instant t = (to != null) ? Instant.parse(to) : Instant.now().plusSeconds(365L * 24 * 3600);
        List<Slot> slots = slotService.listSlots(calendarId, f, t);
        return slots.stream().map(this::toDto).collect(Collectors.toList());
    }

    private SlotDTO toDto(Slot s) {
        SlotDTO d = new SlotDTO();
        d.id = s.getId();
        d.calendarId = s.getCalendarId();
        d.startTime = s.getStartTime();
        d.endTime = s.getEndTime();
        d.capacity = s.getCapacity();
        d.reservedCount = s.getReservedCount();
        d.status = s.getStatus();
        return d;
    }
}
