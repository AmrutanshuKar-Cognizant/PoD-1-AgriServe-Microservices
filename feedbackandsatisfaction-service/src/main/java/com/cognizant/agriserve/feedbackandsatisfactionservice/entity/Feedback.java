package com.cognizant.agriserve.feedbackandsatisfactionservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    private Long sessionId;
    private Long farmerId;
    private Long trainingProgramId;
    private Integer rating;
    @Column(columnDefinition = "TEXT")
    private String comments;
    private LocalDate date;
}
