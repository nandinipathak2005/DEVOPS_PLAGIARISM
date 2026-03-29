package com.project.async;

import com.project.model.Job;

@FunctionalInterface
public interface JobTask {
    void run(Job job);
}