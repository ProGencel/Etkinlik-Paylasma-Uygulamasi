package com.ahmetefe.backend.restController;

import com.ahmetefe.backend.dto.UserRegisterDto;
import com.ahmetefe.backend.repository.UserRepository;
import com.ahmetefe.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("user")
@RequiredArgsConstructor
@RestController
public class UserRestController {

    final UserRepository userRepository;
    final UserService userService;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody UserRegisterDto userRegisterDto)
    {
        return userService.register(userRegisterDto);
    }


}
