package com.project.service;

import com.project.model.Job;
import com.project.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Long createJob() {

        Job job = new Job();
        job.setStatus("PENDING");
        job.setCreatedAt(LocalDateTime.now());

        jobRepository.save(job);

        return job.getId();
    }
}