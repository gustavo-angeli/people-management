package com.gusta.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTO {
    private Long id;
    @NotBlank(message = "Null or empty username")
    private String name;
    @NotBlank(message = "Null or empty email address")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Null or empty password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
