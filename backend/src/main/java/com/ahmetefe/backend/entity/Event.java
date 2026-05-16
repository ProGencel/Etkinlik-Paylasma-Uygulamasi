package com.ahmetefe.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String place;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.ACTIVE;

    @Enumerated(EnumType.STRING)
    private EventCategory eventCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    private List<User> participants = new ArrayList<>();

    private LocalDate eventDate;
    private LocalTime eventTime;

    @Transient
    private Long remainingDays;

    public Long getRemainingDays()
    {
        if(eventDate == null)
        {
            return 0L;
        }

        LocalDate today = LocalDate.now();
        long remainingDays = ChronoUnit.DAYS.between(today,eventDate);


        return remainingDays > 0 ? remainingDays : 0;
    }

}

enum EventState {
    ACTIVE,
    STOPPED,
    ARCHIVE
}

enum EventCategory {
    GAMEJAM,
    MEETING,
    SPORT,
    CONCERT,
    THEATER
}