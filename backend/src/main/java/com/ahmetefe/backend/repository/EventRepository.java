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

    Page<Event> findByOwnerUser_IdEquals(Long id, Pageable pageable);

    Page<Event> findByTitleContainsIgnoreCaseOrPlaceContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String title, String place, String description, Pageable pageable, EventState state);

    @Query("SELECT e FROM Event e WHERE e.ownerUser.id = :userId " +
            "ORDER BY CASE e.state " +
            "  WHEN 'ACTIVE' THEN 1 " +
            "  WHEN 'ARCHIVE' THEN 2 " +
            "  WHEN 'STOPPED' THEN 3 " +
            "  ELSE 4 END ASC")
    Page<Event> findByOwnerUserIdWithCustomOrder(@Param("userId") Long userId, Pageable pageable);
}