package com.uma.airesumescreening;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // POST /api/jobs - Create a new job
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job created = jobService.createJob(job);
        return ResponseEntity.ok(created);
    }

    // GET /api/jobs - Get all jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // GET /api/jobs/{id} - Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> jobOpt = jobService.getJobById(id);
        if (jobOpt.isPresent()) {
            return ResponseEntity.ok(jobOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/jobs/{id} - Delete a job by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        boolean deleted = jobService.deleteJob(id);
        if (deleted) {
            return ResponseEntity.ok("Job deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}