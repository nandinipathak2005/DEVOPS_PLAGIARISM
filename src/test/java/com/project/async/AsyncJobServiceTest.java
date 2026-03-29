package com.project.async;

import com.project.model.Job;
import com.project.repository.JobRepository;
import com.project.service.DeltaAnalysisService;
import com.project.service.JPlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncJobServiceTest {

    @Mock
    private JPlagService jplagService;

    @Mock
    private DeltaAnalysisService deltaAnalysisService;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private AsyncJobService asyncJobService;

    private File mockSubmissionsDir;
    private Job mockJob;

    @BeforeEach
    void setUp() {
        mockSubmissionsDir = new File("jobs/test-job/submissions");
        mockJob = new Job();
        mockJob.setId(1L);
        mockJob.setStatus("PENDING");
    }

    @Test
    @DisplayName("Successful job should update status to PROCESSING then COMPLETED")
    void testSuccessfulJobUpdatesStatusToCompleted() throws Exception {
        Long jobId = 1L;

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(mockJob));
        when(jplagService.runJPlag(any(), eq("1"), anyBoolean()))
                .thenReturn(Map.of("peerMatches", Map.of()));
        when(deltaAnalysisService.analyze(any(), eq("1")))
                .thenReturn(List.of());
        when(deltaAnalysisService.formatReport(any())).thenReturn("=== Report ===");

        asyncJobService.processJobAsync(jobId, mockSubmissionsDir, false);

        verify(mockJob, times(1)).setStatus("PROCESSING");
        verify(mockJob, times(1)).setStatus("COMPLETED");
        verify(jobRepository, times(2)).save(mockJob);
    }

    @Test
    @DisplayName("JPlag failure should set job status to FAILED")
    void testJplagFailureSetsStatusToFailed() throws Exception {
        Long jobId = 2L;
        Job job = new Job();
        job.setId(jobId);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(jplagService.runJPlag(any(), eq("2"), anyBoolean()))
                .thenThrow(new RuntimeException("JPlag process crashed"));

        asyncJobService.processJobAsync(jobId, mockSubmissionsDir, false);

        verify(job).setStatus("PROCESSING");
        verify(job).setStatus("FAILED");
    }

    @Test
    @DisplayName("Delta analysis failure should set job status to FAILED")
    void testDeltaAnalysisFailureSetsStatusToFailed() throws Exception {
        Long jobId = 3L;
        Job job = new Job();
        job.setId(jobId);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(jplagService.runJPlag(any(), eq("3"), anyBoolean()))
                .thenReturn(Map.of("peerMatches", Map.of()));
        when(deltaAnalysisService.analyze(any(), eq("3")))
                .thenThrow(new RuntimeException("Analysis failed"));

        asyncJobService.processJobAsync(jobId, mockSubmissionsDir, false);

        verify(job).setStatus("FAILED");
    }

    @Test
    @DisplayName("JPlagService should be called with correct submissionsDir and jobId")
    void testJplagCalledWithCorrectArgs() throws Exception {
        Long jobId = 4L;
        Job job = new Job();
        job.setId(jobId);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(jplagService.runJPlag(eq(mockSubmissionsDir), eq("4"), eq(false)))
                .thenReturn(Map.of("peerMatches", Map.of()));
        when(deltaAnalysisService.analyze(any(), any())).thenReturn(List.of());
        when(deltaAnalysisService.formatReport(any())).thenReturn("");

        asyncJobService.processJobAsync(jobId, mockSubmissionsDir, false);

        verify(jplagService).runJPlag(mockSubmissionsDir, "4", false);
    }

    @Test
    @DisplayName("Missing job in repository should not crash the async service")
    void testMissingJobDoesNotCrash() throws Exception {
        Long jobId = 99L;

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());
        when(jplagService.runJPlag(any(), eq("99"), anyBoolean()))
                .thenReturn(Map.of("peerMatches", Map.of()));
        when(deltaAnalysisService.analyze(any(), any())).thenReturn(List.of());
        when(deltaAnalysisService.formatReport(any())).thenReturn("");

        asyncJobService.processJobAsync(jobId, mockSubmissionsDir, false);

        verify(jobRepository, never()).save(any());
    }
}