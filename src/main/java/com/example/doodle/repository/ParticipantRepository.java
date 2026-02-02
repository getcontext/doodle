package com.example.doodle.repository;

import com.example.doodle.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    List<Participant> findByMeetingId(UUID meetingId);
}
