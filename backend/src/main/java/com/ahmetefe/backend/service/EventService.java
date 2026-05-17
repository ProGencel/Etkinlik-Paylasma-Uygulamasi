package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.*;
import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.entity.User;
import com.ahmetefe.backend.repository.EventRepository;
import com.ahmetefe.backend.repository.UserRepository;
import com.ahmetefe.backend.utils.AppConstants;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ahmetefe.backend.dto.UserResponseDto;

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
        EventResponseDto eventResponseDto = modelMapper.map(event,EventResponseDto.class);

        Long userId = (Long) session.getAttribute(AppConstants.USER_SESSION_INFO);
        Optional<User> userOptional = userRepository.findByIdEquals(userId);


        if(userOptional.isPresent())
        {
            event.setOwnerUser(userOptional.get());
            eventRepository.save(event);

            UserOwnerDto userOwnerDto = modelMapper.map(userOptional.get(),UserOwnerDto.class);
            eventResponseDto.setUserOwnerDto(userOwnerDto);

            return ResponseEntity.ok().body(eventResponseDto);
        }
        return ResponseEntity.badRequest().body("Please login first");

    }

}
