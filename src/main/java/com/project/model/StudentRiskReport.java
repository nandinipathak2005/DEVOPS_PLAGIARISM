package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRiskReport {

    private String studentId;
    private RiskLevel riskLevel;

    // All students this student matched with above threshold
    private List<String> matchedWith;

    // Highest similarity score found for this student
    private double maxSimilarity;

    // Number of high-similarity matches
    private int highSimilarityMatchCount;

    // Human-readable reasons (e.g. "Multiple high similarity matches", "Outlier LOC
    // spike")
    private List<String> reasons;

    // Delta analysis fields — populated if LOC/method data is available
    private Integer lineCount;
    private Integer methodCount;
    private boolean outlier; // true if deviates significantly from group average
}