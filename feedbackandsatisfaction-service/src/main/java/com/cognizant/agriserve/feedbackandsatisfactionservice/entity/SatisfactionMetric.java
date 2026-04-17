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
public class SatisfactionMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;
    private Long trainingProgramId ;
    private Long programManagerId;
    private Double score;
    private LocalDate date;
    private String status;
}
