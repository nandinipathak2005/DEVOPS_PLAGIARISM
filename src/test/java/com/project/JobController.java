// This class is used only for tests and should not be picked up by Spring component scanning.
// It remains in the test sources so it can be referenced by tests if needed.
package com.project.test;

import com.project.service.JobService;

public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    public Long createJob(){
        return jobService.createJob();
    }
}