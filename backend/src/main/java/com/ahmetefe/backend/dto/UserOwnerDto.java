package com.ahmetefe.backend.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ahmetefe.backend.entity.User}
 */
@Data
public class UserOwnerDto {
    Long id;
    String name;
    String surname;
    String mail;
}