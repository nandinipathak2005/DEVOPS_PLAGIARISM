package com.project.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.async.AsyncJobService;
import com.project.model.Job;
import com.project.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final AsyncJobService asyncJobService;

    public JobService(JobRepository jobRepository, AsyncJobService asyncJobService) {
        this.jobRepository = jobRepository;
        this.asyncJobService = asyncJobService;
    }

    public Long createJob() {

        Job job = new Job();
        job.setStatus("PENDING");
        job.setCreatedAt(LocalDateTime.now());

        jobRepository.save(job);
        asyncJobService.processJob(job.getId());

        return job.getId();
    }

    public Job getJob(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> {
            throw new JobNotFoundException(id);
        });
    }
}

