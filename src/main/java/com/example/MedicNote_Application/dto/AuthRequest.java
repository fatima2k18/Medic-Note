package com.example.MedicNote_Application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Username must not be blank")

    private String username;

    @NotBlank(message = "Password must not be blank")

    private String password;
    private String role; // ✅ Add this field

    }

