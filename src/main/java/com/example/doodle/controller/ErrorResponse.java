package com.example.doodle.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ErrorResponse(Instant timestamp, int status, String error, String message, String path, String exceptionClass, UUID traceId, List<String> details) {}
