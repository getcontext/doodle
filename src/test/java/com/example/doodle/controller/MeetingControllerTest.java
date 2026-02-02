package com.example.doodle.controller;

import com.example.doodle.dto.MeetingCreateRequest;
import com.example.doodle.model.Meeting;
import com.example.doodle.model.Participant;
import com.example.doodle.repository.ParticipantRepository;
import com.example.doodle.service.MeetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MeetingController.class)
public class MeetingControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @MockBean private MeetingService meetingService;
    @MockBean private ParticipantRepository participantRepository;
    @MockBean private com.example.doodle.repository.MeetingRepository meetingRepository;

    @Test
    public void createMeeting_happyPath_withSlot() throws Exception {
        MeetingCreateRequest req = new MeetingCreateRequest("Standup", null, UUID.randomUUID(), UUID.randomUUID(), null, null, null);

        Meeting m = new Meeting();
        m.setTitle(req.title());
        m.setOrganizerId(req.organizerId());
        m.setCalendarId(UUID.randomUUID());
        m.setStartTime(Instant.parse("2026-02-03T10:00:00Z"));
        m.setEndTime(Instant.parse("2026-02-03T10:30:00Z"));

        given(meetingService.scheduleMeeting(any(MeetingCreateRequest.class))).willReturn(m);
        Participant p = new Participant();
        p.setEmail("a@example.com");
        given(participantRepository.findByMeetingId(any(UUID.class))).willReturn(List.of(p));

        mvc.perform(post("/api/v1/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Standup"))
            .andExpect(jsonPath("$.participantEmails[0]").value("a@example.com"));
    }

    @Test
    public void createMeeting_validationError() throws Exception {
        // missing title and slot/start-end -> invalid
        MeetingCreateRequest req = new MeetingCreateRequest(null, null, UUID.randomUUID(), null, null, null, null);

        mvc.perform(post("/api/v1/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    public void createMeeting_slotFull_returnsConflict() throws Exception {
        MeetingCreateRequest req = new MeetingCreateRequest("Standup", null, UUID.randomUUID(), UUID.randomUUID(), null, null, null);

        given(meetingService.scheduleMeeting(any(MeetingCreateRequest.class))).willThrow(new IllegalStateException("Slot is full or not available"));

        mvc.perform(post("/api/v1/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
