package com.cognizant.agriserve.feedbackandsatisfactionservice.repo;

import com.cognizant.agriserve.feedbackandsatisfactionservice.entity.SatisfactionMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Satisfactionmetricrepo extends JpaRepository<SatisfactionMetric,Long> {
    boolean existsByTrainingProgram_ProgramId(Long programId);
}
