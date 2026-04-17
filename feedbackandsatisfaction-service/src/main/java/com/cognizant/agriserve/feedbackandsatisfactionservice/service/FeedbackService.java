package com.cognizant.agriserve.feedbackandsatisfactionservice.service;

import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.FeedbackRequestDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.FeedbackResponseDTO;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDTO addFeedback(FeedbackRequestDTO dt);
    List<FeedbackResponseDTO> getAllFeedback();
}
