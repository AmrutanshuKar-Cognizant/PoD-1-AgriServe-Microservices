package com.cognizant.agriserve.farmerservice.service;

import com.cognizant.agriserve.farmerservice.dto.request.FarmerUpdateRequestDTO;
import com.cognizant.agriserve.farmerservice.dto.request.RegisterFarmerDTO;
import com.cognizant.agriserve.farmerservice.dto.response.FarmerResponseDTO;

import java.util.List;

public interface FarmerService {

    FarmerResponseDTO getFarmerByUserId(Long userId);

    FarmerResponseDTO updateFarmerProfile(Long userId, FarmerUpdateRequestDTO updateDto);

    List<FarmerResponseDTO> getAllFarmers();

    FarmerResponseDTO getFarmerById(Long farmerId);

    Void registerfarmer(RegisterFarmerDTO registerrequestdto);
}