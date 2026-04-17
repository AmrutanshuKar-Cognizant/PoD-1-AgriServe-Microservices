package com.cognizant.agriserve.userservice.controller;

import com.cognizant.agriserve.userservice.dto.RegisterRequestDTO;
import com.cognizant.agriserve.userservice.dto.UserDTO;
import com.cognizant.agriserve.userservice.dto.UserRequestDTO;
import com.cognizant.agriserve.userservice.dto.UserResponseDTO;
import com.cognizant.agriserve.userservice.entity.User;
import com.cognizant.agriserve.userservice.service.impl.UserServiceImpl;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/{userId}/name")
    @PreAuthorize("hasAnyRole('Admin', 'SERVICE')")
    public ResponseEntity<String> getUserNameById(
            @PathVariable Long userId) {

        log.info("Request received to fetch user name for ID: {}", userId);
        String name = userService.getUserNameById(userId);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('Admin', 'SERVICE')")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long userId) {

        log.info("Request received to fetch user by ID: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('Admin', 'SERVICE')")
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @PathVariable String email) {

        log.info("Request received to fetch user by email: {}", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        log.info("Request received to fetch all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @PathVariable User.Role role) {

        log.info("Request received to fetch users by role: {}", role);
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(
            @PathVariable String status) {

        log.info("Request received to fetch users by status: {}", status);
        return ResponseEntity.ok(userService.getUsersByStatus(status));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {

        log.info("Request received to update user with ID: {}", userId);
        return ResponseEntity.ok(
                userService.updateUser(userId, userRequestDTO));
    }

    @PutMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable Long userId) {

        log.info("Request received to deactivate user with ID: {}", userId);
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId) {

        log.warn("Request received to delete user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/byusername/{username}")
    @PreAuthorize("hasAnyAuthority('SERVICE', 'ROLE_SERVICE')")
    public ResponseEntity<UserDTO> login(@PathVariable String username){

        log.info("Request received from the Auth Service to fetch credentials for: {}", username);
        return ResponseEntity.ok(userService.findbyusername(username));
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('SERVICE', 'ROLE_SERVICE')")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequestDTO registerRequest){
        log.info("Registration request received from Auth Service for email: {}", registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequest));
    }
}