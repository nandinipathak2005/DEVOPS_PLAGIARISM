package com.project.async;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.project.model.Job;
import com.project.repository.JobRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AsyncJobServiceTest {

    @Mock 
    JobRepository jobRepository;
    
    @Mock 
    JobTask jobTask;

    @InjectMocks 
    AsyncJobService asyncJobService;

    @Test
    void processJob_moves_processing_to_completed() {
        List<String> statuses = new ArrayList<>();
        doAnswer(invocation -> {
            Job arg = invocation.getArgument(0);
            statuses.add(arg.getStatus());
            return arg;
        }).when(jobRepository).save(any(Job.class));

        Job job = new Job();
        job.setId(10L);
        job.setStatus("PENDING");
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        asyncJobService.processJob(10L);

        verify(jobRepository, times(2)).save(any(Job.class));
        assertEquals(List.of("PROCESSING", "COMPLETED"), statuses);
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void processJob_sets_failed_when_task_throws() {
        List<String> statuses = new ArrayList<>();
        doAnswer(invocation -> {
            Job arg = invocation.getArgument(0);
            statuses.add(arg.getStatus());
            return arg;
        }).when(jobRepository).save(any(Job.class));

        Job job = new Job();
        job.setId(11L);
        job.setStatus("PENDING");
        when(jobRepository.findById(11L)).thenReturn(Optional.of(job));
        doThrow(new RuntimeException("boom")).when(jobTask).run(any(Job.class));

        asyncJobService.processJob(11L);

        verify(jobRepository, times(2)).save(any(Job.class));
        assertEquals(List.of("PROCESSING", "FAILED"), statuses);
        verifyNoMoreInteractions(jobRepository);
    }
}