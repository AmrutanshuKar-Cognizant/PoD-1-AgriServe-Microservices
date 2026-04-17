package com.cognizant.agriserve.farmerservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmerDocumentUploadRequestDTO {
    private Long farmerId;

    @NotBlank(message = "Document type is required (e.g., [Aadhaar Redacted], Land Permit)")
    private String docType;

    @NotBlank(message = "File URI is required")
    private String fileURI;

}