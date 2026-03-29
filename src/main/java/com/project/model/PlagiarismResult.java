package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismResult {

    // Student/submission identifiers — populated from JPlag's peerMatches map
    private String firstSubmissionId;
    private String secondSubmissionId;

    // AVG similarity score from JPlag (0.0 to 1.0)
    private double similarityScore;

    // The jobId this result belongs to
    private String jobId;

    // Human-readable pair key e.g. "student1 <-> student2"
    public String getPairKey() {
        return firstSubmissionId + " <-> " + secondSubmissionId;
    }
}