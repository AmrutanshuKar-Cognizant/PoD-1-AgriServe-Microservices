package com.cognizant.agriserve.feedbackandsatisfactionservice.controller;

import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.FeedbackRequestDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.FeedbackResponseDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Slf4j
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/Submit")
    public ResponseEntity<FeedbackResponseDTO> submitfeedback(@RequestBody @Valid FeedbackRequestDTO dto) {
        log.info("REST request to submit feedback for Farmer ID: {}", dto.getFarmerId());
        return ResponseEntity.ok(feedbackService.addFeedback(dto));
    }

    @GetMapping("/all")
    public List<FeedbackResponseDTO> getAll() {
        log.info("REST request to fetch all feedback records");
        return feedbackService.getAllFeedback();
    }
}
