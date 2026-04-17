package com.cognizant.agriserve.farmerservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmerUpdateRequestDTO {

    @NotBlank(message = "Name is required for profile update")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Date of birth is required")
    private String dob;

    @NotBlank(message = "Gender is required (MALE, FEMALE, OTHER)")
    private String gender;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact info is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String contactInfo;

    @NotNull(message = "Land size is required")
    @PositiveOrZero(message = "Land size cannot be negative")
    private Double landSize;

    @NotBlank(message = "Crop type is required")
    private String cropType;
}