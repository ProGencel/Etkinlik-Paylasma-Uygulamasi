package com.ahmetefe.backend.restController;

import com.ahmetefe.backend.dto.EventResponseDto;
import com.ahmetefe.backend.dto.UserLoginDto;
import com.ahmetefe.backend.dto.UserRegisterDto;
import com.ahmetefe.backend.repository.UserRepository;
import com.ahmetefe.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("logout")
    public ResponseEntity logout()
    {
        return userService.logout();
    }

    @GetMapping("list")
    public Page<EventResponseDto> listOwnEvents(@RequestParam (defaultValue = "0")int page)
    {
        return userService.listOwnEvents(page);
    }


}
