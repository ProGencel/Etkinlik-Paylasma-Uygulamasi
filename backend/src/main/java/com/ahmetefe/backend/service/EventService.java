package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.*;
import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.entity.User;
import com.ahmetefe.backend.repository.EventRepository;
import com.ahmetefe.backend.repository.UserRepository;
import com.ahmetefe.backend.utils.AppConstants;
import com.ahmetefe.backend.utils.EventState;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    final EventRepository eventRepository;
    final UserRepository userRepository;
    final ModelMapper modelMapper;
    final HttpSession session;

    public ResponseEntity save(EventSaveDto eventSaveDto)
    {
        Event event = modelMapper.map(eventSaveDto,Event.class);

        Long userId = (Long) session.getAttribute(AppConstants.USER_SESSION_INFO);
        Optional<User> userOptional = userRepository.findByIdEquals(userId);


        if(userOptional.isPresent())
        {
            event.setOwnerUser(userOptional.get());
            event = eventRepository.save(event);

            EventResponseDto eventResponseDto = modelMapper.map(event,EventResponseDto.class);

            UserOwnerDto userOwnerDto = modelMapper.map(userOptional.get(),UserOwnerDto.class);
            eventResponseDto.setOwnerUser(userOwnerDto);

            return ResponseEntity.ok().body(eventResponseDto);
        }
        return ResponseEntity.badRequest().body("Please login first");
    }

    @Cacheable(value = "eventList", key = "#eventState + '-' + #page")
    public Page<EventResponseDto> listEvents(EventState eventState, int page)
    {
        Pageable pageable = Pageable.ofSize(AppConstants.EVENTS_PER_PAGE).withPage(page);
        Page<Event> eventPage = eventRepository.findByStateEquals(eventState,pageable);

        Page<EventResponseDto> eventResponseDtoPage = eventPage.map(event -> modelMapper.map(event,EventResponseDto.class));

        return eventResponseDtoPage;
    }

    public ResponseEntity joinEvent(long eventId)
    {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if(eventOptional.isEmpty())
        {
            return ResponseEntity.badRequest().body("The event you are trying to join does not exist.");
        }

        Event event = eventOptional.get();

        List<User> eventParticipants = event.getParticipants();
        Long userId = (Long)session.getAttribute(AppConstants.USER_SESSION_INFO);

        Optional<User> userOptional = userRepository.findByIdEquals(userId);
        if(userOptional.isEmpty())
        {
            return ResponseEntity.badRequest().body("Please login first");
        }


        if(eventParticipants.contains(userOptional.get()))
        {
            return ResponseEntity.badRequest().body("You have already joined this event");
        }

        eventParticipants.add(userOptional.get());

        eventRepository.save(event);

        EventResponseDto eventResponseDto = modelMapper.map(event,EventResponseDto.class);

        return ResponseEntity.ok().body(eventResponseDto);
    }

}
