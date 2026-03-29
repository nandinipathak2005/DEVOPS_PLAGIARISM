package com.project.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.model.Job;
import com.project.service.JobService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(JobController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean JobService jobService;

    @Test
    void post_jobs_returns_id() throws Exception {
        when(jobService.createJob()).thenReturn(7L);

        mockMvc.perform(post("/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }

    @Test
    void get_job_returns_job_json() throws Exception {
        Job job = new Job();
        job.setId(9L);
        job.setStatus("COMPLETED");
        job.setCreatedAt(LocalDateTime.of(2026, 3, 18, 12, 0));
        when(jobService.getJob(9L)).thenReturn(job);

        mockMvc.perform(get("/jobs/9").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.createdAt").exists());
    }
}

