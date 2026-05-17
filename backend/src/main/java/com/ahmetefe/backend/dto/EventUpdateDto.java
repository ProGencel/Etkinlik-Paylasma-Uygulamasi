package com.ahmetefe.backend.dto;

import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.utils.EventCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link Event}
 */
@Data
public class EventUpdateDto{
    @NotNull
    @Positive
    private Long id;
    @NotNull
    @Size(min = 3, max = 20)
    @NotEmpty
    @NotBlank
    String title;
    @NotNull
    @Size(min = 3, max = 100)
    @NotEmpty
    @NotBlank
    String place;
    @NotNull
    @Size(max = 500)
    @NotEmpty
    @NotBlank
    String description;
    @NotNull
    EventCategory eventCategory;
    @FutureOrPresent
    LocalDate eventDate;
    LocalTime eventTime;
}