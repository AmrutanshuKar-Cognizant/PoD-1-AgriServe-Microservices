package com.cognizant.agriserve.feedbackandsatisfactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponseDTO {
    private Long FeedbackId;
    private String FarmerName;
    private String programName;
    private int rating;
    private String Comments;
}
