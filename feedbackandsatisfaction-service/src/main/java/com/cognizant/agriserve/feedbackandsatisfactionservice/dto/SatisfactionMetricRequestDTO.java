package com.cognizant.agriserve.feedbackandsatisfactionservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatisfactionMetricRequestDTO {
    @NotNull(message = "Feedback ID is required")
    private Long programId;

    private String status;
}
