package com.cognizant.agriserve.farmerservice.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data

public class FarmerDocumentResponseDTO {

    private Long documentId;

    private String docType;

    private String fileURI;

    private LocalDate uploadedDate;

    private String verificationStatus;

    private Long farmerId;

}
