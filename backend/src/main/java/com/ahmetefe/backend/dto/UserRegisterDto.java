package com.ahmetefe.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ahmetefe.backend.entity.User}
 */
@Value
public class UserRegisterDto implements Serializable {
    @NotNull
    @Size(min = 2, max = 100)
    @NotEmpty
    @NotBlank
    String name;

    @NotNull
    @Size(min = 2, max = 100)
    @NotEmpty
    @NotBlank
    String surname;

    @NotNull
    @Size(min = 2, max = 100)
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