package com.uma.airesumescreening;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/screening")
public class ScreeningController {

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private ScreeningRepository screeningRepository;

    // POST /api/screening/analyze - trigger AI analysis for a resume against a job
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeResume(@RequestBody Map<String, Long> request) {
        try {
            Long jobId = request.get("jobId");
            Long resumeId = request.get("resumeId");

            if (jobId == null || resumeId == null) {
                return ResponseEntity.badRequest().body("jobId and resumeId are required");
            }

            Screening result = screeningService.analyzeResume(jobId, resumeId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Analysis failed: " + e.getMessage());
        }
    }

    // GET /api/screening/job/{jobId} - all candidates ranked by score for this job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Screening>> getRankedCandidates(@PathVariable Long jobId) {
        return ResponseEntity.ok(screeningRepository.findByJobIdOrderByMatchScoreDesc(jobId));
    }

    // GET /api/screening/{id} - single screening details
    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreening(@PathVariable Long id) {
        Optional<Screening> screeningOpt = screeningRepository.findById(id);
        return screeningOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}