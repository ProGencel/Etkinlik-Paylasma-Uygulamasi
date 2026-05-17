package com.ahmetefe.backend.repository;

import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.utils.EventState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("SELECT e FROM Event e WHERE e.state = 'ACTIVE' AND (e.eventDate < :today OR (e.eventDate = :today AND e.eventTime < :now))")
    List<Event> findExpiredEvents(@Param("today") LocalDate today, @Param("now") LocalTime now);

    Page<Event> findByStateEquals(EventState state,
                                  Pageable pageable);
}