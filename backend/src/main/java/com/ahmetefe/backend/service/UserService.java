package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.*;
import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.entity.User;
import com.ahmetefe.backend.repository.EventRepository;
import com.ahmetefe.backend.repository.UserRepository;
import com.ahmetefe.backend.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final ModelMapper modelMapper;
    final HttpServletRequest request;
    final HttpSession session;
    private final EventRepository eventRepository;

    public ResponseEntity register(UserRegisterDto userRegisterDto)
    {
        Optional<User> userOptional = userRepository.findByMailEqualsIgnoreCase(userRegisterDto.getMail());
        if(userOptional.isEmpty())
        {
            User user = modelMapper.map(userRegisterDto,User.class);
            String hashPassword = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt());
            user.setPassword(hashPassword);
            userRepository.save(user);

            UserResponseDto userResponseDto = modelMapper.map(user,UserResponseDto.class);

            return ResponseEntity.ok().body(userResponseDto);
        }
        else
        {
            Map<String,Object> errorMessage = Map.of("Success", false,"error message:","Your mail is already in use");
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    public ResponseEntity login(UserLoginDto userLoginDto)
    {
        Optional<User> userOptional = userRepository.findByMailEqualsIgnoreCase(userLoginDto.getMail());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            boolean isPasswordMatch = BCrypt.checkpw(userLoginDto.getPassword(),user.getPassword());
            if(isPasswordMatch)
            {
                UserResponseDto userResponseDto = modelMapper.map(user,UserResponseDto.class);

                HttpSession session = request.getSession(true);
                session.setAttribute(AppConstants.USER_SESSION_INFO,userResponseDto.getId());

                return ResponseEntity.ok().body(userResponseDto);
            }
            Map<String,Object> errorMessage = Map.of("success",false,"error message:","Incorrect email or password");
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Map<String,Object> errorMessage = Map.of("success",false,"error message:","mail not found");
        return ResponseEntity.badRequest().body(errorMessage);
    }

    public ResponseEntity logout()
    {
        if(session.getAttribute(AppConstants.USER_SESSION_INFO) != null)
        {
            session.invalidate();
            return ResponseEntity.ok().body("Logout successfull");
        }

        return ResponseEntity.badRequest().body("You are not logged in. Please log in first.");
    }

    public Page<EventResponseDto> listOwnEvents(int page)
    {
        Long userId = (Long)session.getAttribute(AppConstants.USER_SESSION_INFO);
        Pageable pageable = Pageable.ofSize(10).withPage(page);


        Page<Event> eventPage = eventRepository.findByOwnerUserIdWithCustomOrder(userId,pageable);
        Page<EventResponseDto> eventResponseDtoPage = eventPage.map(event -> modelMapper.map(event, EventResponseDto.class));

        return eventResponseDtoPage;
    }
}
