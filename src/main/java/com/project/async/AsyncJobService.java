package com.project.async;

import com.project.model.Job;
import com.project.model.StudentRiskReport;
import com.project.repository.JobRepository;
import com.project.service.DeltaAnalysisService;
import com.project.service.JPlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncJobService {

    private final JPlagService jplagService;
    private final DeltaAnalysisService deltaAnalysisService;
    private final JobRepository jobRepository;

    @Async("plagiarismTaskExecutor")
    public void processJobAsync(Long jobId, File submissionsDir, boolean showReport) {
        log.info("[AsyncJob] Starting async processing for job: {}", jobId);

        try {
            // 1. Mark job as PROCESSING
            updateJobStatus(jobId, "PROCESSING");

            // 2. Run JPlag — Member 4's service takes String jobId
            log.info("[AsyncJob] Invoking JPlagService for job: {}", jobId);
            Map<String, Object> jplagOutput = jplagService.runJPlag(
                    submissionsDir,
                    String.valueOf(jobId),
                    showReport);

            // 3. Run delta analysis
            log.info("[AsyncJob] Running delta analysis for job: {}", jobId);
            List<StudentRiskReport> reports = deltaAnalysisService.analyze(
                    jplagOutput,
                    String.valueOf(jobId));

            // 4. Log the report
            String formattedReport = deltaAnalysisService.formatReport(reports);
            log.info("[AsyncJob] Report for job {}:\n{}", jobId, formattedReport);

            // 5. Mark COMPLETED
            updateJobStatus(jobId, "COMPLETED");
            log.info("[AsyncJob] Job {} completed successfully.", jobId);

        } catch (Exception e) {
            log.error("[AsyncJob] Job {} failed: {}", jobId, e.getMessage(), e);
            updateJobStatus(jobId, "FAILED");
        }
    }

    private void updateJobStatus(Long jobId, String status) {
        try {
            jobRepository.findById(jobId).ifPresent(job -> {
                job.setStatus(status);
                jobRepository.save(job);
                log.info("[AsyncJob] Job {} status → {}", jobId, status);
            });
        } catch (Exception e) {
            log.warn("[AsyncJob] Could not update status for job {}: {}", jobId, e.getMessage());
        }
    }
}