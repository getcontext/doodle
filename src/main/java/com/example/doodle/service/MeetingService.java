package com.example.doodle.service;

import com.example.doodle.dto.MeetingCreateRequest;
import com.example.doodle.model.Meeting;
import com.example.doodle.model.Participant;
import com.example.doodle.model.Slot;
import com.example.doodle.repository.MeetingRepository;
import com.example.doodle.repository.ParticipantRepository;
import com.example.doodle.repository.SlotRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final SlotRepository slotRepository;

    public MeetingService(MeetingRepository meetingRepository, ParticipantRepository participantRepository, SlotRepository slotRepository) {
        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public Meeting scheduleMeeting(MeetingCreateRequest req) {
        // if slotId provided, reserve it atomically
        if (req.slotId != null) {
            // try increment reserved count
            int updated = slotRepository.tryIncrementReservedCount(req.slotId);
            if (updated == 0) {
                throw new IllegalStateException("Slot is full or not available");
            }
            // load slot for times
            Slot slot = slotRepository.findById(req.slotId).orElseThrow(() -> new IllegalStateException("Slot not found"));
            Meeting m = new Meeting();
            m.setCalendarId(slot.getCalendarId());
            m.setTitle(req.title);
            m.setOrganizerId(req.organizerId);
            m.setStartTime(slot.getStartTime());
            m.setEndTime(slot.getEndTime());
            m.setStatus("SCHEDULED");
            Meeting saved;
            try {
                saved = meetingRepository.save(m);
            } catch (DataIntegrityViolationException ex) {
                // likely exclusion constraint violation
                throw new IllegalStateException("Meeting conflicts with existing meeting");
            }
            // add participants
            List<Participant> parts = new ArrayList<>();
            if (req.participantEmails != null) {
                for (String email : req.participantEmails) {
                    Participant p = new Participant();
                    p.setMeetingId(saved.getId());
                    p.setEmail(email);
                    parts.add(p);
                }
                participantRepository.saveAll(parts);
            }
            return saved;
        }

        // otherwise schedule by start/end, rely on DB exclusion constraint
        Meeting m = new Meeting();
        m.setCalendarId(req.calendarId);
        m.setTitle(req.title);
        m.setOrganizerId(req.organizerId);
        m.setStartTime(req.startTime);
        m.setEndTime(req.endTime);
        m.setStatus("SCHEDULED");
        try {
            Meeting saved = meetingRepository.save(m);
            if (req.participantEmails != null) {
                List<Participant> parts = new ArrayList<>();
                for (String email : req.participantEmails) {
                    Participant p = new Participant();
                    p.setMeetingId(saved.getId());
                    p.setEmail(email);
                    parts.add(p);
                }
                participantRepository.saveAll(parts);
            }
            return saved;
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("Meeting conflicts with existing meeting");
        }
    }
}
