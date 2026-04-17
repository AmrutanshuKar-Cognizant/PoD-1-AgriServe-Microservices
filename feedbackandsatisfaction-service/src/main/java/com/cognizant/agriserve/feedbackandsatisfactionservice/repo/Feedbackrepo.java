package com.cognizant.agriserve.feedbackandsatisfactionservice.repo;

import com.cognizant.agriserve.feedbackandsatisfactionservice.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Feedbackrepo extends JpaRepository<Feedback,Long> {
    List<Feedback> findByTrainingProgram_ProgramId(Long programId);
}
