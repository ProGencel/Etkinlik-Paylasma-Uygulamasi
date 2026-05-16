package com.ahmetefe.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ahmetefe.backend.entity.User}
 */
@Data
public class UserResponseDto {
    @NotNull
    Long id;
    @NotNull
    @Size(min = 2)
    @NotEmpty
    @NotBlank
    String name;
    @NotNull
    @Size(min = 2)
    @NotEmpty
    @NotBlank
    String surname;
    @NotNull
    @Size(min = 2)
    @Email
    @NotEmpty
    @NotBlank
    String mail;
}