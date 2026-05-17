package com.ahmetefe.backend.dto;

import com.ahmetefe.backend.utils.EventCategory;
import com.ahmetefe.backend.utils.EventState;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO for {@link com.ahmetefe.backend.entity.Event}
 */
@Data
public class EventResponseDto{
    Long id;
    String title;
    String place;
    String description;
    EventCategory eventCategory;
    @FutureOrPresent
    LocalDate eventDate;
    LocalTime eventTime;

    UserOwnerDto OwnerUser;
    List<UserOwnerDto> participants;
    EventState eventState;
}