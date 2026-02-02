package com.example.doodle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateCalendarRequest(
        @NotBlank String name,
        @NotNull UUID ownerId,
        String defaultTimeZone
) {}
