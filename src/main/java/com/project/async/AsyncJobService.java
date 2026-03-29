package com.project.async;

import com.project.model.Job;
import com.project.repository.JobRepository;
import java.util.Optional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncJobService {

    private final JobRepository jobRepository;
    private final JobTask jobTask;

    public AsyncJobService(JobRepository jobRepository, JobTask jobTask) {
        this.jobRepository = jobRepository;
        this.jobTask = jobTask;
    }

    @Async
    public void processJob(Long jobId) {
        Optional<Job> maybeJob = jobRepository.findById(jobId);
        if (maybeJob.isEmpty()) {
            return;
        }

        Job job = maybeJob.get();
        job.setStatus("PROCESSING");
        jobRepository.save(job);

        try {
            jobTask.run(job);
            job.setStatus("COMPLETED");
            jobRepository.save(job);
        } catch (Exception ex) {
            job.setStatus("FAILED");
            jobRepository.save(job);
        }
    }
}