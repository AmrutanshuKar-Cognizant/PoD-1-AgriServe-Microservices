package com.cognizant.agriserve.userservice.dto;

import lombok.Data;


import com.cognizant.agriserve.userservice.entity.User;


@Data
public class UserResponseDTO {

    private Long userId;
    private String name;
    private User.Role role;
    private String email;
    private String phone;
    private String status;

}
