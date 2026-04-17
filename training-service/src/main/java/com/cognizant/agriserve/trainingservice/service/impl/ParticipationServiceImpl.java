package com.cognizant.agriserve.trainingservice.service.impl;

import com.cognizant.agriserve.trainingservice.client.UserClient;
import com.cognizant.agriserve.trainingservice.dao.ParticipationRepository;
import com.cognizant.agriserve.trainingservice.dao.WorkshopRepository;
import com.cognizant.agriserve.trainingservice.dto.request.AttendanceUpdateRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.request.ParticipationRequestDTO;
import com.cognizant.agriserve.trainingservice.dto.response.ParticipationResponseDTO;
import com.cognizant.agriserve.trainingservice.entity.Participation;
import com.cognizant.agriserve.trainingservice.entity.Workshop;
import com.cognizant.agriserve.trainingservice.exception.ResourceConflictException;
import com.cognizant.agriserve.trainingservice.exception.ResourceNotFoundException;
import com.cognizant.agriserve.trainingservice.exception.UnauthorizedActionException; // <-- Added Import
import com.cognizant.agriserve.trainingservice.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final WorkshopRepository workshopRepository;
    private final UserClient userClient;
    private final ModelMapper modelMapper;

    @Override
    public ParticipationResponseDTO registerForWorkshop(ParticipationRequestDTO requestDto) {
        Workshop workshop = workshopRepository.findById(requestDto.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "ID", requestDto.getWorkshopId()));

        if (!userClient.checkUserExists(requestDto.getFarmerId())) {
            throw new ResourceNotFoundException("Farmer ID " + requestDto.getFarmerId() + " does not exist in User System.");
        }

        boolean alreadyRegistered = participationRepository
                .existsByWorkshop_WorkshopIdAndFarmerId(requestDto.getWorkshopId(), requestDto.getFarmerId());

        if (alreadyRegistered) {
            throw new ResourceConflictException("Farmer is already registered for this workshop.");
        }

        Participation newRegistration = new Participation();
        newRegistration.setWorkshop(workshop);
        newRegistration.setFarmerId(requestDto.getFarmerId());
        newRegistration.setAttendanceStatus("Registered");

        Participation savedRegistration = participationRepository.save(newRegistration);

        return convertToDto(savedRegistration);
    }

    @Override
    public List<ParticipationResponseDTO> getParticipantsForWorkshop(Long workshopId) {
        return participationRepository.findByWorkshop_WorkshopId(workshopId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationResponseDTO updateAttendance(AttendanceUpdateRequestDTO requestDto, Long requesterId, boolean isAdmin) {
        Participation existingRecord = participationRepository.findById(requestDto.getParticipationId())
                .orElseThrow(() -> new ResourceNotFoundException("Participation", "ID", requestDto.getParticipationId()));

        // OWNERSHIP VERIFICATION: Only the officer assigned to this specific workshop can update attendance
        if (!isAdmin && !existingRecord.getWorkshop().getOfficerId().equals(requesterId)) {
            throw new UnauthorizedActionException("Only the assigned Extension Officer can update attendance for this workshop.");
        }

        existingRecord.setAttendanceStatus(requestDto.getNewAttendanceStatus());
        Participation updatedRecord = participationRepository.save(existingRecord);

        return convertToDto(updatedRecord);
    }

    @Override
    public List<ParticipationResponseDTO> getParticipationByFarmerId(Long farmerId) {
        return participationRepository.findByFarmerId(farmerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ParticipationResponseDTO convertToDto(Participation participation) {
        ParticipationResponseDTO dto = modelMapper.map(participation, ParticipationResponseDTO.class);
        if (participation.getWorkshop() != null) {
            dto.setWorkshopId(participation.getWorkshop().getWorkshopId());
        }
        return dto;
    }
}