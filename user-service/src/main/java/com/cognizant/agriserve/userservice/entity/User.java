package com.cognizant.agriserve.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public enum Role {
        Admin, ExtensionOfficer, ComplianceOfficer, Farmer, Auditor, ProgramManager
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian number"
    )
    @Column(unique = true)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @NotBlank(message = "Status cannot be empty")
    private String status;
}