package com.uma.airesumescreening;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "resume_id", nullable = false)
    private Long resumeId;

    @Column(name = "match_score")
    private Integer matchScore;

    @Column(name = "matching_skills", columnDefinition = "TEXT")
    private String matchingSkills;

    @Column(name = "missing_skills", columnDefinition = "TEXT")
    private String missingSkills;

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    @Column(name = "interview_questions", columnDefinition = "TEXT")
    private String interviewQuestions;

    @Column(name = "screened_at")
    private LocalDateTime screenedAt;

    @PrePersist
    protected void onCreate() {
        screenedAt = LocalDateTime.now();
    }
}