package com.cognizant.agriserve.userservice.dto;

import com.cognizant.agriserve.userservice.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data

public class UserRequestDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Role is required")
    private User.Role role;
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian number")
    private String phone;
    @NotBlank(message = "Status cannot be empty")
    private String status;

}
