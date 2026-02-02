package com.example.doodle.repository;

import com.example.doodle.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CalendarRepository extends JpaRepository<Calendar, UUID> {
}
