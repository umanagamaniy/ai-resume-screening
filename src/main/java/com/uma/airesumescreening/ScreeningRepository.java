package com.uma.airesumescreening;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    // Custom query: find all screenings for a job, sorted by score (highest first)
    List<Screening> findByJobIdOrderByMatchScoreDesc(Long jobId);
}