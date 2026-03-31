
package com.plagiarism.plagiarismchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.plagiarism.plagiarismchecker.entity.SimilarityResult;

public interface SimilarityResultRepository extends JpaRepository<SimilarityResult, Long> {
}