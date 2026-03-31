
package com.plagiarism.plagiarismchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.plagiarism.plagiarismchecker.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
