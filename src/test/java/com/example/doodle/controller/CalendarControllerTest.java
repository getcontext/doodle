package com.example.doodle.controller;

import com.example.doodle.dto.CreateCalendarRequest;
import com.example.doodle.dto.CreateSlotRequest;
import com.example.doodle.model.Calendar;
import com.example.doodle.model.Slot;
import com.example.doodle.service.SlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CalendarController.class)
public class CalendarControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @MockBean private SlotService slotService;
    @MockBean private com.example.doodle.repository.CalendarRepository calendarRepository;

    @Test
    public void createCalendar_happyPath() throws Exception {
        CreateCalendarRequest req = new CreateCalendarRequest("Team", UUID.randomUUID(), null);

        Calendar saved = new Calendar();
        saved.setName(req.name());
        saved.setOwnerId(req.ownerId());

        given(calendarRepository.save(any(Calendar.class))).willReturn(saved);

        mvc.perform(post("/api/v1/calendars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Team"));
    }

    @Test
    public void createCalendar_validationError() throws Exception {
        // missing name: pass null for name
        CreateCalendarRequest req = new CreateCalendarRequest(null, UUID.randomUUID(), null);

        mvc.perform(post("/api/v1/calendars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    public void createSlot_happyPath() throws Exception {
        CreateSlotRequest req = new CreateSlotRequest(Instant.parse("2026-02-03T10:00:00Z"), Instant.parse("2026-02-03T11:00:00Z"), 2);

        Slot s = new Slot();
        s.setCalendarId(UUID.randomUUID());
        s.setStartTime(req.startTime());
        s.setEndTime(req.endTime());
        s.setCapacity(req.capacity());

        given(slotService.createSlot(any(UUID.class), any(CreateSlotRequest.class))).willReturn(s);

        mvc.perform(post("/api/v1/calendars/" + s.getCalendarId() + "/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.capacity").value(2));
    }
}
