package com.cognizant.agriserve.trainingservice.controller;

import com.cognizant.agriserve.trainingservice.dto.request.AttendanceUpdateRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.request.ParticipationRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.response.ParticipationResponseDTO;
import com.cognizant.agriserve.trainingservice.service.ParticipationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/participations")
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('Farmer')")
    public ResponseEntity<ParticipationResponseDTO> registerForWorkshop(
            @RequestHeader("X-Logged-In-User-Id") Long farmerId,
            @Valid @RequestBody ParticipationRequestDTO requestDto) {

        requestDto.setFarmerId(farmerId);

        log.info("Farmer [ID={}] registering for Workshop ID: {}", farmerId, requestDto.getWorkshopId());
        return new ResponseEntity<>(participationService.registerForWorkshop(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/workshop/{workshopId}")
    @PreAuthorize("hasAnyRole('ExtensionOfficer', 'ProgramManager', 'Admin')")
    public ResponseEntity<List<ParticipationResponseDTO>> getParticipantsForWorkshop(
            @PathVariable Long workshopId) {

        return ResponseEntity.ok(participationService.getParticipantsForWorkshop(workshopId));
    }

    @GetMapping("/farmer/{farmerId}")
    @PreAuthorize("hasAnyRole('ExtensionOfficer', 'ProgramManager', 'Admin')")
    public ResponseEntity<List<ParticipationResponseDTO>> getParticipationByFarmerId(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(participationService.getParticipationByFarmerId(farmerId));
    }

    @PutMapping("/attendance")
    @PreAuthorize("hasAnyRole('ExtensionOfficer', 'Admin')")
    public ResponseEntity<ParticipationResponseDTO> updateAttendance(
            @RequestHeader("X-Logged-In-User-Id") Long officerId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role,
            @Valid @RequestBody AttendanceUpdateRequestDTO requestDto) {

        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("Officer [ID={}] (Admin: {}) updating attendance for Participation ID: {}", officerId, isAdmin, requestDto.getParticipationId());

        return ResponseEntity.ok(participationService.updateAttendance(requestDto, officerId, isAdmin));
    }
}