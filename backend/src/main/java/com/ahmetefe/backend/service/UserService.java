package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.UserRegisterDto;
import com.ahmetefe.backend.entity.User;
import com.ahmetefe.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final ModelMapper modelMapper;

    public ResponseEntity register(UserRegisterDto userRegisterDto)
    {
        Optional<User> userOptional = userRepository.findByMail(userRegisterDto.getMail());
        if(userOptional.isEmpty())
        {
            User user = modelMapper.map(userRegisterDto,User.class);
            userRepository.save(user);
            return ResponseEntity.ok().body(user);
        }
        else
        {
            Map<String,Object> errorMessage = Map.of("Success", false,"Error Message:","Your mail is already in use");
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
