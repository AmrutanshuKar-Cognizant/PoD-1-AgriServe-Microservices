package com.cognizant.agriserve.trainingservice.controller;

import com.cognizant.agriserve.trainingservice.dto.request.TrainingProgramRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.response.TrainingProgramResponseDTO;
import com.cognizant.agriserve.trainingservice.exception.ResourceNotFoundException;
import com.cognizant.agriserve.trainingservice.service.TrainingProgramService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/programs")
public class TrainingProgramController {

    private final TrainingProgramService programService;

    public TrainingProgramController(TrainingProgramService programService) {
        this.programService = programService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<TrainingProgramResponseDTO> createProgram(
            @RequestHeader("X-Logged-In-User-Id") Long managerId,
            @Valid @RequestBody TrainingProgramRequestDTO requestDto) {

        requestDto.setManagerId(managerId);

        log.info("ProgramManager [ID={}] creating Training Program: {}", managerId, requestDto.getTitle());
        TrainingProgramResponseDTO savedProgram = programService.createProgram(requestDto);
        return new ResponseEntity<>(savedProgram, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ProgramManager', 'Admin', 'Farmer', 'ExtensionOfficer')")
    public ResponseEntity<List<TrainingProgramResponseDTO>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }

    @GetMapping("/{programId}")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<TrainingProgramResponseDTO> getProgramById(@PathVariable Long programId) {
        return ResponseEntity.ok(programService.getProgramById(programId));
    }

    @PutMapping("/{programId}")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<TrainingProgramResponseDTO> updateProgram(
            @PathVariable Long programId,
            @RequestHeader("X-Logged-In-User-Id") Long requesterId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role,
            @Valid @RequestBody TrainingProgramRequestDTO requestDto) {

        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("User {} (Admin: {}) is updating Training Program ID: {}", requesterId, isAdmin, programId);
        return ResponseEntity.ok(programService.updateProgram(programId, requestDto, requesterId, isAdmin));
    }

    @DeleteMapping("/{programId}")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<Void> deleteProgram(
            @PathVariable Long programId,
            @RequestHeader("X-Logged-In-User-Id") Long requesterId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role) {

        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("User {} (Admin: {}) is deleting Training Program ID: {}", requesterId, isAdmin, programId);
        programService.deleteProgram(programId, requesterId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{programId}/exists")
    @PreAuthorize("hasAnyRole('ComplianceOfficer', 'SERVICE')")
    public ResponseEntity<Void> checkProgramExists(@PathVariable Long programId) {
        boolean exists = programService.checkProgramExists(programId);
        if (exists) return ResponseEntity.ok().build();
        else throw new ResourceNotFoundException("Training Program", "ID", programId);
    }

    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin', 'ComplianceOfficer', 'SERVICE')")
    public ResponseEntity<List<TrainingProgramResponseDTO>> getCompletedPrograms() {
        return ResponseEntity.ok(programService.getProgramsByStatus("Completed"));
    }
}