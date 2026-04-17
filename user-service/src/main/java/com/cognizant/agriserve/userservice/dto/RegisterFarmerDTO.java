package com.cognizant.agriserve.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterFarmerDTO {
    private String name;
    private String dob;
    private String gender;
    private String address;
    private String contactInfo;
    private Double landSize;
    private String cropType;
    private Long UserId;
}
