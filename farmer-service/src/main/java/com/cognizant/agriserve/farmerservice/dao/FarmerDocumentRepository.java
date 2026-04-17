package com.cognizant.agriserve.farmerservice.dao;

import com.cognizant.agriserve.farmerservice.entity.FarmerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmerDocumentRepository extends JpaRepository<FarmerDocument, Long> {

    List<FarmerDocument> findByFarmer_FarmerId(Long farmerId);

    List<FarmerDocument> findByVerificationStatus(FarmerDocument.VerificationStatus status);
}