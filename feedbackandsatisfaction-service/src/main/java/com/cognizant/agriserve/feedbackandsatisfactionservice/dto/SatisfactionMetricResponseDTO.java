package com.cognizant.agriserve.feedbackandsatisfactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SatisfactionMetricResponseDTO {
    private Long programId;
    private String status;
    private Double score;
}
