package com.project.async;

import com.project.model.Job;
import org.springframework.stereotype.Component;

@Component
public class DefaultJobTask implements JobTask {
    @Override
    public void run(Job job) {
        // Intentionally left as no-op; plug real processing here.
    }
}

