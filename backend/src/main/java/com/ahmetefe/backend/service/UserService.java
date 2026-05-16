package com.ahmetefe.backend.service;

import com.ahmetefe.backend.dto.UserLoginDto;
import com.ahmetefe.backend.dto.UserRegisterDto;
import com.ahmetefe.backend.dto.UserResponseDto;
import com.ahmetefe.backend.entity.User;
import com.ahmetefe.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
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

    public ResponseEntity register(UserRegisterDto userRegisterDto)
    {
        Optional<User> userOptional = userRepository.findByMailIgnoreCase(userRegisterDto.getMail());
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
        Optional<User> userOptional = userRepository.findByMailIgnoreCase(userLoginDto.getMail());

        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            boolean isPasswordMatch = BCrypt.checkpw(userLoginDto.getPassword(),user.getPassword());
            if(isPasswordMatch)
            {
                /*
                * Customer nesnesinde hem karmasik sifre oldugu icin hemde ileride gereksiz baska bilgiler barindiracagi icin
                * sunucunun ramini sisirmemesi adina responseDto yu sessiona ekliyoruz.
                */

                UserResponseDto userResponseDto = modelMapper.map(user,UserResponseDto.class);

                HttpSession session = request.getSession(true);
                session.setAttribute("user",userResponseDto);

                return ResponseEntity.ok().body(userResponseDto);
            }
            Map<String,Object> errorMessage = Map.of("success",false,"error message:","Incorrect email or password");
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Map<String,Object> errorMessage = Map.of("success",false,"error message:","mail not found");
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
