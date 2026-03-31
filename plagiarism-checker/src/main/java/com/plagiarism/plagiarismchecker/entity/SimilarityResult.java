
package com.plagiarism.plagiarismchecker.entity;

import jakarta.persistence.*;

@Entity
@Table(name="similarity_results")
public class SimilarityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double similarityScore;

    @ManyToOne
    @JoinColumn(name="submission1_id")
    private Submission submission1;

    @ManyToOne
    @JoinColumn(name="submission2_id")
    private Submission submission2;

    public SimilarityResult(){}

    public Long getId() {
        return id;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public Submission getSubmission1() {
        return submission1;
    }

    public Submission getSubmission2() {
        return submission2;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public void setSubmission1(Submission submission1) {
        this.submission1 = submission1;
    }

    public void setSubmission2(Submission submission2) {
        this.submission2 = submission2;
    }
}
