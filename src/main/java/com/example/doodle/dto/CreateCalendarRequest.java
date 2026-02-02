package com.example.doodle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CreateCalendarRequest {
    @NotBlank
    public String name;

    @NotNull
    public UUID ownerId;

    public String defaultTimeZone;
}
