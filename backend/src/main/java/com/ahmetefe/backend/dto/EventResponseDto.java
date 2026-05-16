package com.ahmetefe.backend.dto;

import com.ahmetefe.backend.utils.EventCategory;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link com.ahmetefe.backend.entity.Event}
 */
@Data
public class EventResponseDto{
    String title;
    String place;
    String description;
    EventCategory eventCategory;
    @FutureOrPresent
    LocalDate eventDate;
    LocalTime eventTime;

    UserOwnerDto userOwnerDto;
}