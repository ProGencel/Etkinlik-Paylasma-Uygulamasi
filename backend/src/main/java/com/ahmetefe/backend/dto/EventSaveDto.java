package com.ahmetefe.backend.dto;

import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.utils.EventCategory;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link Event}
 */
@Value
public class EventSaveDto implements Serializable {
    @NotNull
    @Size(min = 3, max = 20)
    @NotEmpty
    @NotBlank
    String title;
    @NotNull
    @Size(min = 3, max = 20)
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
    @NotNull
    @FutureOrPresent
    LocalDate eventDate;
    @NotNull
    LocalTime eventTime;
}