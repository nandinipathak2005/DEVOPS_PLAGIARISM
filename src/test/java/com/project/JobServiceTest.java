package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.async.AsyncJobService;
import com.project.model.Job;
import com.project.repository.JobRepository;
import com.project.service.JobService;
import com.project.service.JobNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock JobRepository jobRepository;
    @Mock AsyncJobService asyncJobService;

    @InjectMocks JobService jobService;

    @Test
    void createJob_setsPending_saves_and_triggers_async() {
        when(jobRepository.save(any(Job.class)))
                .thenAnswer(invocation -> {
                    Job j = invocation.getArgument(0);
                    j.setId(123L);
                    return j;
                });
        doNothing().when(asyncJobService).processJob(123L);

        Long id = jobService.createJob();

        assertEquals(123L, id);

        ArgumentCaptor<Job> captor = ArgumentCaptor.forClass(Job.class);
        verify(jobRepository).save(captor.capture());
        Job saved = captor.getValue();
        assertEquals("PENDING", saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        verify(asyncJobService).processJob(123L);
    }

    @Test
    void getJob_returns_job_when_found() {
        Job job = new Job();
        job.setId(5L);
        job.setStatus("COMPLETED");
        when(jobRepository.findById(5L)).thenReturn(Optional.of(job));

        Job found = jobService.getJob(5L);

        assertEquals(5L, found.getId());
        assertEquals("COMPLETED", found.getStatus());
    }

    @Test
    void getJob_throws_when_missing() {
        when(jobRepository.findById(999L)).thenReturn(Optional.empty());
        JobNotFoundException ex = assertThrows(JobNotFoundException.class, () -> jobService.getJob(999L));
        assertEquals("Job not found: 999", ex.getMessage());
    }
}
