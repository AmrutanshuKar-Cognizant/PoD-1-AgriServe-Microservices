package com.cognizant.agriserve.feedbackandsatisfactionservice.controller;

import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.SatisfactionMetricRequestDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.SatisfactionMetricResponseDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.service.SatisfactionMetricservice;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api/satisfaction-metrics")
public class Satisfactionmetriccontroller {

    @Autowired
    private SatisfactionMetricservice metricservice;

    @PostMapping("/evaluate")
    public ResponseEntity<SatisfactionMetricResponseDTO> evaluateProgram(@RequestBody @Valid SatisfactionMetricRequestDTO dto) {
        log.info("Received request to evaluate Program ID: {}", dto.getProgramId());
        return ResponseEntity.ok(metricservice.evaluate(dto));
    }
}
