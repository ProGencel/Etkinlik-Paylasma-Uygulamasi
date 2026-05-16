package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.EventSaveDto;
import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    final EventRepository eventRepository;
    final ModelMapper modelMapper;

    public ResponseEntity save(EventSaveDto eventSaveDto)
    {
        Event event = modelMapper.map(eventSaveDto,Event.class);
        eventRepository.save(event);

        return ResponseEntity.ok().body(event);
    }

}
