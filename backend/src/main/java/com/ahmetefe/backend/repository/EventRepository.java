package com.ahmetefe.backend.repository;

import com.ahmetefe.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {

}
