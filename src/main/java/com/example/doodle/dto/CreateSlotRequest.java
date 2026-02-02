package com.example.doodle.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class CreateSlotRequest {
    @NotNull
    public Instant startTime;
    @NotNull
    public Instant endTime;
    @Min(1)
    public int capacity = 1;
}
