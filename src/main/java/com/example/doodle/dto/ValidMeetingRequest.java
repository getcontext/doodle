package com.example.doodle.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = MeetingRequestValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidMeetingRequest {
    String message() default "Either slotId or both startTime and endTime must be provided, and endTime must be after startTime";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
