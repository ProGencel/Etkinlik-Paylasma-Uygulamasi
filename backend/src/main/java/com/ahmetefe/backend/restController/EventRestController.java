package com.ahmetefe.backend.restController;

import com.ahmetefe.backend.dto.EventSaveDto;
import com.ahmetefe.backend.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("event")
@RequiredArgsConstructor
public class EventRestController {

    final EventService eventService;

    @PostMapping("save")
    public ResponseEntity save(@Valid @RequestBody EventSaveDto eventSaveDto)
    {
        return eventService.save(eventSaveDto);
    }

}
