package com.cognizant.agriserve.trainingservice.service.impl;

import com.cognizant.agriserve.trainingservice.client.UserClient;
import com.cognizant.agriserve.trainingservice.dao.TrainingProgramRepository;
import com.cognizant.agriserve.trainingservice.dao.WorkshopRepository;
import com.cognizant.agriserve.trainingservice.dto.request.WorkshopRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.response.WorkshopResponseDTO;
import com.cognizant.agriserve.trainingservice.entity.TrainingProgram;
import com.cognizant.agriserve.trainingservice.entity.Workshop;
import com.cognizant.agriserve.trainingservice.exception.ResourceNotFoundException;
import com.cognizant.agriserve.trainingservice.exception.UnauthorizedActionException; // <-- Added Import
import com.cognizant.agriserve.trainingservice.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final TrainingProgramRepository programRepository;
    private final UserClient userClient;
    private final ModelMapper modelMapper;

    @Override
    public List<WorkshopResponseDTO> getAllWorkshops() {
        return workshopRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<WorkshopResponseDTO> getActiveWorkshopsForFarmers() {
        return workshopRepository.findAll().stream()
                .filter(w -> "Scheduled".equals(w.getStatus()) || "Ongoing".equals(w.getStatus()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public WorkshopResponseDTO scheduleWorkshop(WorkshopRequestDTO requestDto, Long requesterId, boolean isAdmin) {
        TrainingProgram program = programRepository.findById(requestDto.getProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Training Program", "ID", requestDto.getProgramId()));

        // OWNERSHIP VERIFICATION: You can only schedule a workshop if you own the parent program
        if (!isAdmin && !program.getManagerId().equals(requesterId)) {
            throw new UnauthorizedActionException("You are not authorized to schedule a workshop for a program you did not create.");
        }

        if (!userClient.checkUserExists(requestDto.getOfficerId())) {
            throw new ResourceNotFoundException("Officer ID " + requestDto.getOfficerId() + " not found in User System.");
        }

        Workshop newWorkshop = modelMapper.map(requestDto, Workshop.class);
        newWorkshop.setTrainingProgram(program);
        newWorkshop.setStatus("Scheduled");

        Workshop savedWorkshop = workshopRepository.save(newWorkshop);
        return convertToDto(savedWorkshop);
    }

    @Override
    public List<WorkshopResponseDTO> getWorkshopsByOfficer(Long officerId) {
        return workshopRepository.findByOfficerId(officerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public WorkshopResponseDTO updateWorkshopStatus(Long workshopId, String status, Long requesterId, boolean isAdmin) {
        Workshop existingWorkshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "ID", workshopId));

        // OWNERSHIP VERIFICATION: Either the Program Manager OR the assigned Extension Officer can update the status
        boolean isOwner = existingWorkshop.getTrainingProgram().getManagerId().equals(requesterId);
        boolean isAssignedOfficer = existingWorkshop.getOfficerId().equals(requesterId);

        if (!isAdmin && !isOwner && !isAssignedOfficer) {
            throw new UnauthorizedActionException("Only the program manager or the assigned extension officer can update the workshop status.");
        }

        existingWorkshop.setStatus(status);
        Workshop updatedWorkshop = workshopRepository.save(existingWorkshop);
        return convertToDto(updatedWorkshop);
    }

    @Override
    public WorkshopResponseDTO updateWorkshop(Long workshopId, WorkshopRequestDTO requestDto, Long requesterId, boolean isAdmin) {
        Workshop existingWorkshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "ID", workshopId));

        // OWNERSHIP VERIFICATION
        if (!isAdmin && !existingWorkshop.getTrainingProgram().getManagerId().equals(requesterId)) {
            throw new UnauthorizedActionException("You are not authorized to edit a workshop for a program you did not create.");
        }

        if (!existingWorkshop.getOfficerId().equals(requestDto.getOfficerId())) {
            if (!userClient.checkUserExists(requestDto.getOfficerId())) {
                throw new ResourceNotFoundException("New Officer ID not found in User System.");
            }
        }

        existingWorkshop.setLocation(requestDto.getLocation());
        existingWorkshop.setDate(requestDto.getDate());
        existingWorkshop.setOfficerId(requestDto.getOfficerId());

        Workshop updatedWorkshop = workshopRepository.save(existingWorkshop);
        return convertToDto(updatedWorkshop);
    }

    @Override
    public void deleteWorkshop(Long workshopId, Long requesterId, boolean isAdmin) {
        Workshop existingWorkshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "ID", workshopId));

        // OWNERSHIP VERIFICATION
        if (!isAdmin && !existingWorkshop.getTrainingProgram().getManagerId().equals(requesterId)) {
            throw new UnauthorizedActionException("You are not authorized to delete a workshop for a program you did not create.");
        }

        workshopRepository.delete(existingWorkshop);
    }

    private WorkshopResponseDTO convertToDto(Workshop workshop) {
        WorkshopResponseDTO dto = modelMapper.map(workshop, WorkshopResponseDTO.class);
        if (workshop.getTrainingProgram() != null) {
            dto.setProgramTitle(workshop.getTrainingProgram().getTitle());
            dto.setProgramId(workshop.getTrainingProgram().getProgramId());
        }
        return dto;
    }
}