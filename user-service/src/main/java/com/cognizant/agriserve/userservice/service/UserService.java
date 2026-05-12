package com.cognizant.agriserve.userservice.service;

import com.cognizant.agriserve.userservice.dto.*;
import com.cognizant.agriserve.userservice.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequestDTO requestDTO);

    String getUserNameById(Long userId);

    UserResponseDTO getUserById(Long userId);

    UserResponseDTO getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    List<UserResponseDTO> getUsersByRole(User.Role role);

    List<UserResponseDTO> getUsersByStatus(String status);

    UserResponseDTO updateUser(Long userId, UserRequestDTO updatedUserDTO);

//    void deactivateUser(Long userId);

    void deleteUser(Long userId);

    UserDTO register(RegisterRequestDTO dto);

    UserDTO findbyusername(String Username);
}