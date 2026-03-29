package com.project.service;

import com.project.model.PlagiarismResult;
import com.project.model.RiskLevel;
import com.project.model.StudentRiskReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeltaAnalysisService {

    // Thresholds are configurable via application.properties — easy to tune
    @Value("${plagiarism.threshold.high:0.80}")
    private double highThreshold;

    @Value("${plagiarism.threshold.medium:0.50}")
    private double mediumThreshold;

    @Value("${plagiarism.threshold.multiMatch:2}")
    private int multiMatchCount;

    /**
     * Main entry point.
     * Takes JPlag's peerMatches map (pair string -> similarity score)
     * and converts to per-student risk reports.
     *
     * peerMatches format from Member 4:
     * key = "student1 <-> student2"
     * value = similarity score (0.0 to 1.0)
     */
    public List<StudentRiskReport> analyze(Map<String, Object> jplagOutput, String jobId) {
        log.info("[DeltaAnalysis] Starting analysis for job: {}", jobId);

        // Step 1: parse raw peerMatches into neutral PlagiarismResult objects
        List<PlagiarismResult> results = parsePeerMatches(jplagOutput, jobId);

        // Step 2: group by student
        Map<String, List<PlagiarismResult>> byStudent = groupByStudent(results);

        // Step 3: compute risk per student
        List<StudentRiskReport> reports = byStudent.entrySet().stream()
                .map(entry -> buildReport(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(StudentRiskReport::getRiskLevel).reversed())
                .collect(Collectors.toList());

        // Step 4: delta / outlier detection across the group
        applyOutlierDetection(reports);

        log.info("[DeltaAnalysis] Completed for job {}. {} students flagged HIGH risk.",
                jobId,
                reports.stream().filter(r -> r.getRiskLevel() == RiskLevel.HIGH).count());

        return reports;
    }

    // -------------------------------------------------------------------------
    // Step 1: Parse peerMatches
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private List<PlagiarismResult> parsePeerMatches(Map<String, Object> jplagOutput, String jobId) {
        List<PlagiarismResult> results = new ArrayList<>();

        Object rawMatches = jplagOutput.get("peerMatches");
        if (rawMatches == null) {
            log.warn("[DeltaAnalysis] No peerMatches found in JPlag output for job {}", jobId);
            return results;
        }

        Map<String, Object> peerMatches = (Map<String, Object>) rawMatches;

        for (Map.Entry<String, Object> entry : peerMatches.entrySet()) {
            String pairKey = entry.getKey(); // "student1 <-> student2"
            double score = toDouble(entry.getValue());

            String[] parts = pairKey.split("<->");
            if (parts.length != 2) {
                log.warn("[DeltaAnalysis] Unexpected pair key format: {}", pairKey);
                continue;
            }

            results.add(PlagiarismResult.builder()
                    .firstSubmissionId(parts[0].trim())
                    .secondSubmissionId(parts[1].trim())
                    .similarityScore(score)
                    .jobId(jobId)
                    .build());
        }

        return results;
    }

    // -------------------------------------------------------------------------
    // Step 2: Group by student
    // -------------------------------------------------------------------------

    private Map<String, List<PlagiarismResult>> groupByStudent(List<PlagiarismResult> results) {
        Map<String, List<PlagiarismResult>> map = new HashMap<>();

        for (PlagiarismResult r : results) {
            // Each pair counts toward BOTH students
            map.computeIfAbsent(r.getFirstSubmissionId(), k -> new ArrayList<>()).add(r);
            map.computeIfAbsent(r.getSecondSubmissionId(), k -> new ArrayList<>()).add(r);
        }

        return map;
    }

    // -------------------------------------------------------------------------
    // Step 3: Build per-student risk report
    // -------------------------------------------------------------------------

    private StudentRiskReport buildReport(String studentId, List<PlagiarismResult> matches) {
        double maxSim = matches.stream()
                .mapToDouble(PlagiarismResult::getSimilarityScore)
                .max()
                .orElse(0.0);

        List<PlagiarismResult> highMatches = matches.stream()
                .filter(r -> r.getSimilarityScore() >= highThreshold)
                .collect(Collectors.toList());

        List<String> matchedWith = matches.stream()
                .filter(r -> r.getSimilarityScore() >= mediumThreshold)
                .map(r -> r.getFirstSubmissionId().equals(studentId)
                        ? r.getSecondSubmissionId()
                        : r.getFirstSubmissionId())
                .distinct()
                .collect(Collectors.toList());

        List<String> reasons = buildReasons(maxSim, highMatches);

        RiskLevel risk = computeRisk(maxSim, highMatches.size());

        return StudentRiskReport.builder()
                .studentId(studentId)
                .riskLevel(risk)
                .matchedWith(matchedWith)
                .maxSimilarity(maxSim)
                .highSimilarityMatchCount(highMatches.size())
                .reasons(reasons)
                .outlier(false) // set in step 4
                .build();
    }

    private RiskLevel computeRisk(double maxSim, int highMatchCount) {
        if (maxSim >= highThreshold && highMatchCount >= multiMatchCount)
            return RiskLevel.HIGH;
        if (maxSim >= highThreshold)
            return RiskLevel.HIGH;
        if (maxSim >= mediumThreshold)
            return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }

    private List<String> buildReasons(double maxSim, List<PlagiarismResult> highMatches) {
        List<String> reasons = new ArrayList<>();
        if (maxSim >= highThreshold) {
            reasons.add(String.format("Max similarity %.0f%% exceeds high threshold", maxSim * 100));
        }
        if (highMatches.size() >= multiMatchCount) {
            reasons.add(String.format("Multiple high-similarity matches (%d)", highMatches.size()));
        }
        return reasons;
    }

    // -------------------------------------------------------------------------
    // Step 4: Outlier detection across the group
    // -------------------------------------------------------------------------

    private void applyOutlierDetection(List<StudentRiskReport> reports) {
        if (reports.isEmpty())
            return;

        // Compute group average similarity
        double avg = reports.stream()
                .mapToDouble(StudentRiskReport::getMaxSimilarity)
                .average()
                .orElse(0.0);

        double stdDev = computeStdDev(reports, avg);

        for (StudentRiskReport report : reports) {
            // Flag as outlier if more than 2 standard deviations above group average
            boolean isOutlier = report.getMaxSimilarity() > avg + (2 * stdDev);
            report.setOutlier(isOutlier);

            if (isOutlier) {
                report.getReasons().add(
                        String.format("Statistical outlier — %.0f%% vs group avg %.0f%%",
                                report.getMaxSimilarity() * 100, avg * 100));
                // Escalate to HIGH if not already
                if (report.getRiskLevel() != RiskLevel.HIGH) {
                    report.setRiskLevel(RiskLevel.HIGH);
                }
            }
        }
    }

    private double computeStdDev(List<StudentRiskReport> reports, double avg) {
        double variance = reports.stream()
                .mapToDouble(r -> Math.pow(r.getMaxSimilarity() - avg, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    private double toDouble(Object val) {
        if (val instanceof Number)
            return ((Number) val).doubleValue();
        try {
            return Double.parseDouble(val.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Formats the suspicious students report as a human-readable string.
     * Matches the deliverable format from the project spec.
     */
    public String formatReport(List<StudentRiskReport> reports) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Suspicious Students Summary ===\n\n");

        reports.stream()
                .filter(r -> r.getRiskLevel() != RiskLevel.LOW)
                .forEach(r -> {
                    sb.append(String.format("Student: %s%n", r.getStudentId()));
                    sb.append(String.format("Risk: %s%n", r.getRiskLevel()));
                    sb.append(String.format("Matched with: %s%n", String.join(", ", r.getMatchedWith())));
                    sb.append(String.format("Max Similarity: %.0f%%%n", r.getMaxSimilarity() * 100));
                    sb.append(String.format("Reason: %s%n", String.join(", ", r.getReasons())));
                    if (r.isOutlier())
                        sb.append("⚠ Statistical outlier detected\n");
                    sb.append("\n");
                });

        return sb.toString();
    }
}