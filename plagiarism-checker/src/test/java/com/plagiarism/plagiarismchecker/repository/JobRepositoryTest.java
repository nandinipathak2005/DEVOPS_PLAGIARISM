package com.plagiarism.plagiarismchecker.repository;

import com.plagiarism.plagiarismchecker.entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Test
    void testSaveJob() {

        Job job = new Job();
        job.setJobName("Assignment 1");
        job.setDescription("Plagiarism check");
        job.setCreatedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);

        assertNotNull(savedJob.getId());
        assertEquals("Assignment 1", savedJob.getJobName());
    }
}