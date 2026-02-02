package com.example.doodle.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CreateSlotRequest(
        @NotNull Instant startTime,
        @NotNull Instant endTime,
        @Min(1) int capacity
) {
    public CreateSlotRequest {
        if (capacity < 1) capacity = 1;
    }
}
