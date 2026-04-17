package com.cognizant.agriserve.feedbackandsatisfactionservice.service.impl;

import com.cognizant.agriserve.feedbackandsatisfactionservice.client.TrainingProgramClient;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.SatisfactionMetricRequestDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.dto.SatisfactionMetricResponseDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.entity.Feedback;
import com.cognizant.agriserve.feedbackandsatisfactionservice.entity.SatisfactionMetric;
import com.cognizant.agriserve.feedbackandsatisfactionservice.externaldto.TrainingProgramDTO;
import com.cognizant.agriserve.feedbackandsatisfactionservice.repo.Feedbackrepo;
import com.cognizant.agriserve.feedbackandsatisfactionservice.repo.Satisfactionmetricrepo;
import com.cognizant.agriserve.feedbackandsatisfactionservice.service.SatisfactionMetricservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class SatisfactionMetricimpl implements SatisfactionMetricservice {
    @Autowired
    private Satisfactionmetricrepo metricRepo;
    @Autowired
    private Feedbackrepo feedbackRepo;

    @Autowired
    private TrainingProgramClient programClient;

    @Override
    public SatisfactionMetricResponseDTO evaluate(SatisfactionMetricRequestDTO dto) {
        log.info("Evaluating Metrics for Program ID: {}", dto.getProgramId());

        // 1. VERIFY: Ask the Program Service if this ID is valid
        // If the program doesn't exist, Feign will throw an exception
        TrainingProgramDTO program = programClient.getbyid(dto.getProgramId());

        // 2. FETCH: Local feedback data for this program ID
        // Note: Repository method changed to use Long programId instead of Entity
        List<Feedback> feedbackList = feedbackRepo.findByTrainingProgram_ProgramId(dto.getProgramId());

        if (feedbackList.isEmpty()) {
            log.warn("No feedback entries found for Program: {}", dto.getProgramId());
            throw new RuntimeException("Cannot evaluate: No feedback exists for this program yet.");
        }

        // 3. CALCULATE: Same logic as before
        double averageScore = feedbackList.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);

        // 4. SAVE: Store only the ID in your local Metric table
        SatisfactionMetric metric = new SatisfactionMetric();
        metric.setTrainingProgramId(dto.getProgramId()); // Storing Long, not Entity
        metric.setScore(averageScore);
        metric.setStatus(dto.getStatus() != null ? dto.getStatus() : "EVALUATED");

        SatisfactionMetric savedMetric = metricRepo.save(metric);

        // 5. RETURN: Enrich response with the Name we got from Feign
        return SatisfactionMetricResponseDTO.builder()
                .programId(savedMetric.getTrainingProgramId())
                .status(savedMetric.getStatus())
                .score(savedMetric.getScore())
                .build();
    }

    @Override
    public List<SatisfactionMetricResponseDTO> getSatisfactionmetric() {
        List<SatisfactionMetric> entities = metricRepo.findAll();

        return entities.stream().map(entity -> {
            // Get Program Title for each metric
            TrainingProgramDTO program = programClient.getbyid(entity.getTrainingProgramId());

            return SatisfactionMetricResponseDTO.builder()
                    .programId(entity.getTrainingProgramId())
                    .status(entity.getStatus())
                    .score(entity.getScore())
                    .build();
        }).toList();
    }
}
