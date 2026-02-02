package com.example.doodle.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MeetingRequestValidator implements ConstraintValidator<ValidMeetingRequest, MeetingCreateRequest> {

    @Override
    public boolean isValid(MeetingCreateRequest req, ConstraintValidatorContext context) {
        if (req == null) return true; // other @NotNull handles null
        if (req.slotId != null) return true;
        if (req.startTime != null && req.endTime != null && req.endTime.isAfter(req.startTime)) return true;

        // build nice message
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Either slotId or both startTime and endTime (endTime after startTime) must be provided")
               .addConstraintViolation();
        return false;
    }
}
