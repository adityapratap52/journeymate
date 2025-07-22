package com.journeymate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotBlank(message = "Old password must not be blank")
    private String oldPassword;
    
    @NotBlank(message = "New password must not be blank")
    private String newPassword;
}