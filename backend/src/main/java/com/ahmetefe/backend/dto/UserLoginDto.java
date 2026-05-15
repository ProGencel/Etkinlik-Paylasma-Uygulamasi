package com.ahmetefe.backend.dto;

import com.ahmetefe.backend.entity.User;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
public class UserLoginDto implements Serializable {
    @NotNull
    @Size
    @Email
    @NotEmpty
    @NotBlank
    String mail;
    @NotNull
    @Size(min = 3, max = 20)
    @NotEmpty
    @NotBlank
    String password;
}