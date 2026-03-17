package com.project.controller;

import com.project.service.JobService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping
    public Long createJob(){
        return jobService.createJob();
    }
}