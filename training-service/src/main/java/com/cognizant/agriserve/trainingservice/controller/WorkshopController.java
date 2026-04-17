package com.cognizant.agriserve.trainingservice.controller;

import com.cognizant.agriserve.trainingservice.dto.request.WorkshopRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.response.WorkshopResponseDTO;
import com.cognizant.agriserve.trainingservice.service.WorkshopService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;

    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<WorkshopResponseDTO> scheduleWorkshop(
            @RequestHeader("X-Logged-In-User-Id") Long requesterId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role,
            @Valid @RequestBody WorkshopRequestDTO requestDto) {

        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("Manager [ID={}] scheduling a new workshop for Program ID: {}", requesterId, requestDto.getProgramId());
        WorkshopResponseDTO scheduledWorkshop = workshopService.scheduleWorkshop(requestDto, requesterId, isAdmin);
        return new ResponseEntity<>(scheduledWorkshop, HttpStatus.CREATED);
    }

    @PutMapping("/{workshopId}")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<WorkshopResponseDTO> updateWorkshop(
            @PathVariable Long workshopId,
            @RequestHeader("X-Logged-In-User-Id") Long requesterId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role,
            @Valid @RequestBody WorkshopRequestDTO requestDto) {

        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("Manager [ID={}] requesting to edit Workshop ID: {}", requesterId, workshopId);
        return ResponseEntity.ok(workshopService.updateWorkshop(workshopId, requestDto, requesterId, isAdmin));
    }

    @PatchMapping("/{workshopId}/status")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin', 'ExtensionOfficer')")
    public ResponseEntity<WorkshopResponseDTO> updateWorkshopStatus(
            @PathVariable Long workshopId,
            @RequestParam String status,
            @RequestHeader("X-Logged-In-User-Id") Long requesterId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role) {

        // The requester here could be the Manager owning the program OR the Officer assigned to the class
        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("User [ID={}] updating Workshop ID: {} to status: {}", requesterId, workshopId, status);
        return ResponseEntity.ok(workshopService.updateWorkshopStatus(workshopId, status, requesterId, isAdmin));
    }

    @DeleteMapping("/{workshopId}")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin')")
    public ResponseEntity<Void> deleteWorkshop(
            @PathVariable Long workshopId,
            @RequestHeader("X-Logged-In-User-Id") Long requesterId,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role) {

        boolean isAdmin = role.equalsIgnoreCase("Admin");

        log.info("Manager [ID={}] requesting to delete Workshop ID: {}", requesterId, workshopId);
        workshopService.deleteWorkshop(workshopId, requesterId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin', 'Farmer')")
    public ResponseEntity<List<WorkshopResponseDTO>> getAllWorkshops() {
        return ResponseEntity.ok(workshopService.getAllWorkshops());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin', 'Farmer')")
    public ResponseEntity<List<WorkshopResponseDTO>> getActiveWorkshops() {
        return ResponseEntity.ok(workshopService.getActiveWorkshopsForFarmers());
    }

    @GetMapping("/officer/{officerId}")
    @PreAuthorize("hasAnyRole('ProgramManager', 'Admin', 'ExtensionOfficer')")
    public ResponseEntity<List<WorkshopResponseDTO>> getWorkshopsByOfficer(@PathVariable Long officerId) {
        return ResponseEntity.ok(workshopService.getWorkshopsByOfficer(officerId));
    }
}