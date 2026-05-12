package com.cognizant.agriserve.feedbackandsatisfactionservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatisfactionMetricRequestDTO {
    @NotNull(message = "Feedback ID is required")
    private Long programId;
    private Long managerId;
    private String status;
    private LocalDate date;
}
