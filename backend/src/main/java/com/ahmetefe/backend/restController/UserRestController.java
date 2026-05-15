package com.ahmetefe.backend.restController;

import com.ahmetefe.backend.dto.UserLoginDto;
import com.ahmetefe.backend.dto.UserRegisterDto;
import com.ahmetefe.backend.repository.UserRepository;
import com.ahmetefe.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("login")
    public ResponseEntity login(@Valid @RequestBody UserLoginDto userLoginDto)
    {
        return userService.login(userLoginDto);
    }


}
