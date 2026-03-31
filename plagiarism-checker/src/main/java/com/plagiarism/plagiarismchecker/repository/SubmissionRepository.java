
package com.plagiarism.plagiarismchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.plagiarism.plagiarismchecker.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}