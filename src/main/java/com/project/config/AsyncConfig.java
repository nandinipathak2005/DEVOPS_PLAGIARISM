package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "plagiarismTaskExecutor")
    public Executor plagiarismTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Allow up to 5 plagiarism jobs to run simultaneously
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);

        // Queue up to 25 jobs before rejecting
        executor.setQueueCapacity(25);

        // Prefix makes threads easy to identify in logs
        executor.setThreadNamePrefix("plagiarism-async-");

        // Wait for running jobs to finish before shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }
}
