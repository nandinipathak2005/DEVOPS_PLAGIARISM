package com.project.service;

import com.project.model.RiskLevel;
import com.project.model.StudentRiskReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DeltaAnalysisServiceTest {

    private DeltaAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new DeltaAnalysisService();
        ReflectionTestUtils.setField(service, "highThreshold", 0.80);
        ReflectionTestUtils.setField(service, "mediumThreshold", 0.50);
        ReflectionTestUtils.setField(service, "multiMatchCount", 2);
    }

    private Map<String, Object> buildJplagOutput(Map<String, Double> pairs) {
        Map<String, Object> peerMatches = new HashMap<>(pairs);
        Map<String, Object> output = new HashMap<>();
        output.put("peerMatches", peerMatches);
        return output;
    }

    @Test
    @DisplayName("Student with similarity above 80% should be flagged HIGH")
    void testHighRiskAbove80Percent() {
        Map<String, Double> pairs = Map.of("student1 <-> student2", 0.95);
        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "1");

        StudentRiskReport s1 = findStudent(reports, "student1");
        assertThat(s1).isNotNull();
        assertThat(s1.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(s1.getMaxSimilarity()).isEqualTo(0.95);
    }

    @Test
    @DisplayName("Student with similarity between 50-80% should be flagged MEDIUM")
    void testMediumRiskBetween50And80() {
        Map<String, Double> pairs = Map.of("student1 <-> student2", 0.65);
        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "2");

        StudentRiskReport s1 = findStudent(reports, "student1");
        assertThat(s1.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
    }

    @Test
    @DisplayName("Student with similarity below 50% should be LOW risk")
    void testLowRiskBelow50() {
        Map<String, Double> pairs = Map.of("student1 <-> student2", 0.30);
        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "3");

        StudentRiskReport s1 = findStudent(reports, "student1");
        assertThat(s1.getRiskLevel()).isEqualTo(RiskLevel.LOW);
    }

    @Test
    @DisplayName("Student matching multiple others above threshold stays HIGH")
    void testMultipleHighMatches() {
        Map<String, Double> pairs = Map.of(
                "student1 <-> student2", 0.92,
                "student1 <-> student3", 0.88,
                "student2 <-> student3", 0.45);
        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "4");

        StudentRiskReport s1 = findStudent(reports, "student1");
        assertThat(s1.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        assertThat(s1.getHighSimilarityMatchCount()).isGreaterThanOrEqualTo(2);
        assertThat(s1.getMatchedWith()).contains("student2", "student3");
    }

    @Test
    @DisplayName("Empty JPlag output should produce empty report list")
    void testEmptyPeerMatches() {
        Map<String, Object> output = new HashMap<>();
        output.put("peerMatches", new HashMap<>());

        List<StudentRiskReport> reports = service.analyze(output, "5");
        assertThat(reports).isEmpty();
    }

    @Test
    @DisplayName("Null peerMatches should not throw an exception")
    void testNullPeerMatchesHandled() {
        Map<String, Object> output = new HashMap<>();
        output.put("peerMatches", null);

        List<StudentRiskReport> reports = service.analyze(output, "6");
        assertThat(reports).isEmpty();
    }

    @Test
    @DisplayName("Student significantly above group average should be flagged as outlier")
    void testOutlierDetection() {
        Map<String, Double> pairs = new HashMap<>();
        pairs.put("student1 <-> student2", 0.99);
        pairs.put("student3 <-> student4", 0.10);
        pairs.put("student5 <-> student6", 0.12);
        pairs.put("student7 <-> student8", 0.08);

        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "7");

        StudentRiskReport s1 = findStudent(reports, "student1");
        assertThat(s1.isOutlier()).isTrue();
        assertThat(s1.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
    }

    @Test
    @DisplayName("Formatted report should contain student ID and risk level")
    void testFormatReport() {
        Map<String, Double> pairs = Map.of("studentA <-> studentB", 0.90);
        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "8");

        String formatted = service.formatReport(reports);
        assertThat(formatted).contains("studentA");
        assertThat(formatted).contains("HIGH");
        assertThat(formatted).contains("90%");
    }

    @Test
    @DisplayName("Both students in a matched pair should appear in reports")
    void testBothStudentsInPairAreReported() {
        Map<String, Double> pairs = Map.of("alice <-> bob", 0.85);
        List<StudentRiskReport> reports = service.analyze(buildJplagOutput(pairs), "9");

        List<String> ids = reports.stream()
                .map(StudentRiskReport::getStudentId)
                .toList();
        assertThat(ids).contains("alice", "bob");
    }

    private StudentRiskReport findStudent(List<StudentRiskReport> reports, String studentId) {
        return reports.stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
}