package com.example.doodle.controller;

import com.example.doodle.dto.MeetingCreateRequest;
import com.example.doodle.dto.MeetingDTO;
import com.example.doodle.model.Meeting;
import com.example.doodle.model.Participant;
import com.example.doodle.repository.MeetingRepository;
import com.example.doodle.repository.ParticipantRepository;
import com.example.doodle.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MeetingController {
    private final MeetingService meetingService;
    private final ParticipantRepository participantRepository;
    private final MeetingRepository meetingRepository;

    public MeetingController(MeetingService meetingService, ParticipantRepository participantRepository, MeetingRepository meetingRepository) {
        this.meetingService = meetingService;
        this.participantRepository = participantRepository;
        this.meetingRepository = meetingRepository;
    }

    @PostMapping("/meetings")
    public ResponseEntity<MeetingDTO> createMeeting(@Valid @RequestBody MeetingCreateRequest req) {
        Meeting m = meetingService.scheduleMeeting(req);
        MeetingDTO dto = toDto(m);
        List<Participant> parts = participantRepository.findByMeetingId(m.getId());
        dto.participantEmails = parts.stream().map(Participant::getEmail).collect(Collectors.toList());
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/meetings/{meetingId}")
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable UUID meetingId) {
        return meetingRepository.findById(meetingId).map(m -> {
            MeetingDTO dto = toDto(m);
            List<Participant> parts = participantRepository.findByMeetingId(m.getId());
            dto.participantEmails = parts.stream().map(Participant::getEmail).collect(Collectors.toList());
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/meetings")
    public List<MeetingDTO> listMeetings(@RequestParam UUID calendarId,
                                         @RequestParam(required = false) String from,
                                         @RequestParam(required = false) String to) {
        Instant f = (from != null) ? Instant.parse(from) : Instant.EPOCH;
        Instant t = (to != null) ? Instant.parse(to) : Instant.now().plusSeconds(365L * 24 * 3600);
        List<Meeting> meetings = meetingRepository.findByCalendarIdAndStartTimeBetween(calendarId, f, t);
        return meetings.stream().map(m -> {
            MeetingDTO dto = toDto(m);
            List<Participant> parts = participantRepository.findByMeetingId(m.getId());
            dto.participantEmails = parts.stream().map(Participant::getEmail).collect(Collectors.toList());
            return dto;
        }).collect(Collectors.toList());
    }

    private MeetingDTO toDto(Meeting m) {
        MeetingDTO d = new MeetingDTO();
        d.id = m.getId();
        d.calendarId = m.getCalendarId();
        d.title = m.getTitle();
        d.organizerId = m.getOrganizerId();
        d.startTime = m.getStartTime();
        d.endTime = m.getEndTime();
        d.status = m.getStatus();
        return d;
    }
}
