package com.example.doodle.service;

import com.example.doodle.dto.CreateSlotRequest;
import com.example.doodle.model.Slot;
import com.example.doodle.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SlotService {
    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Transactional
    public Slot createSlot(UUID calendarId, CreateSlotRequest req) {
        Slot s = new Slot();
        s.setCalendarId(calendarId);
        s.setStartTime(req.startTime);
        s.setEndTime(req.endTime);
        s.setCapacity(req.capacity);
        s.setReservedCount(0);
        s.setStatus("AVAILABLE");
        return slotRepository.save(s);
    }

    public List<Slot> listSlots(UUID calendarId, Instant from, Instant to) {
        return slotRepository.findByCalendarIdAndStartTimeBetween(calendarId, from, to);
    }

    @Transactional
    public boolean tryReserveSlot(UUID slotId) {
        int updated = slotRepository.tryIncrementReservedCount(slotId);
        return updated > 0;
    }
}
