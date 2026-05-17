package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.*;
import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.dto.EventUpdateDto;
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
import java.util.Map;
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

        if(!event.getState().equals(EventState.ACTIVE))
        {
            return ResponseEntity.badRequest().body("This event is not active anymore");
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

    public ResponseEntity deleteEvent(Long eventId)
    {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Long userId = (Long) session.getAttribute(AppConstants.USER_SESSION_INFO);
        Optional<User> optionalUser = userRepository.findByIdEquals(userId);

        if(optionalEvent.isEmpty())
        {
            Map<String, Object> hm = Map.of("success", false, "message", "Event does not exist");
            return ResponseEntity.status(404).body(hm);
        }

        if(optionalUser.isEmpty())
        {
            Map<String, Object> hm = Map.of("success", false, "message", "Please login first");
            return ResponseEntity.badRequest().body(hm);
        }

        Event event = optionalEvent.get();
        User user = optionalUser.get();

        if(!user.getOwnEventList().contains(event))
        {
            Map<String, Object> hm = Map.of("success", false, "message", "This event does not belong to you");
            return ResponseEntity.badRequest().body(hm);
        }

        eventRepository.deleteById(eventId);

        Map<String, Object> message = Map.of("success", true, "message", "Event deleted successfully.");
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity updateEvent(EventUpdateDto eventUpdateDto)
    {
        Long userId = (Long) session.getAttribute(AppConstants.USER_SESSION_INFO);
        Optional<Event> eventOptional = eventRepository.findById(eventUpdateDto.getId());
        Optional<User> userOptional = userRepository.findByIdEquals(userId);

        if(eventOptional.isEmpty())
        {
            Map<String, Object> hm = Map.of("success", false, "message", "Event not found");
            return ResponseEntity.status(404).body(hm);
        }

        if(userOptional.isEmpty())
        {
            Map<String, Object> hm = Map.of("success", false, "message", "Please login first");
            return ResponseEntity.status(404).body(hm);
        }

        User user = userOptional.get();
        Event event = eventOptional.get();

        if(!user.getOwnEventList().contains(event))
        {
            Map<String, Object> hm = Map.of("success", false, "message", "This event does not belong to you");
            return ResponseEntity.badRequest().body(hm);
        }

        if(!event.getState().equals(EventState.ACTIVE))
        {
            Map<String, Object> hm = Map.of("success", false, "message", "You cannot update inactive events");
            return ResponseEntity.badRequest().body(hm);
        }

        updateEventFields(event, eventUpdateDto);
        eventRepository.save(event);

        Map<String, Object> message = Map.of("success", true, "message", "Event updated successfully.");
        return ResponseEntity.ok().body(message);
    }

    private void updateEventFields(Event event, EventUpdateDto dto) {
        
        event.setTitle(dto.getTitle());
        event.setPlace(dto.getPlace());
        event.setDescription(dto.getDescription());
        event.setEventCategory(dto.getEventCategory());
        event.setEventDate(dto.getEventDate());
        event.setEventTime(dto.getEventTime());
    }
    
}
