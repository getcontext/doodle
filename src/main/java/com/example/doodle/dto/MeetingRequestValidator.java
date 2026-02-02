package com.example.doodle.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MeetingRequestValidator implements ConstraintValidator<ValidMeetingRequest, MeetingCreateRequest> {

    @Override
    public boolean isValid(MeetingCreateRequest req, ConstraintValidatorContext context) {
        if (req == null) return true; // other @NotNull handles null

        // If slotId is provided, it's valid
        if (req.slotId() != null) return true;

        // Otherwise, validate startTime and endTime and attach property-level violations
        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (req.startTime() == null) {
            context.buildConstraintViolationWithTemplate("startTime is required when slotId is not provided")
                    .addPropertyNode("startTime")
                    .addConstraintViolation();
            valid = false;
        }
        if (req.endTime() == null) {
            context.buildConstraintViolationWithTemplate("endTime is required when slotId is not provided")
                    .addPropertyNode("endTime")
                    .addConstraintViolation();
            valid = false;
        }
        if (req.startTime() != null && req.endTime() != null && !req.endTime().isAfter(req.startTime())) {
            context.buildConstraintViolationWithTemplate("endTime must be after startTime")
                    .addPropertyNode("endTime")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
