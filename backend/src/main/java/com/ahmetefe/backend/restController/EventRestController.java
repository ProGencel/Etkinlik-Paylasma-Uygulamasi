package com.ahmetefe.backend.restController;

import com.ahmetefe.backend.dto.EventResponseDto;
import com.ahmetefe.backend.dto.EventSaveDto;
import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.service.EventService;
import com.ahmetefe.backend.utils.EventState;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("list")
    public Page<EventResponseDto> listEvents(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "ACTIVE") EventState eventState)
    {
         return eventService.listEvents(eventState,page);
    }

}
