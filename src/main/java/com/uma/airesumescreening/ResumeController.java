package com.uma.airesumescreening;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    // POST /api/resumes/upload - Upload a resume file
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("candidateName") String candidateName,
            @RequestParam(value = "candidateEmail", required = false) String candidateEmail,
            @RequestParam("file") MultipartFile file) {
        try {
            Resume resume = resumeService.uploadResume(candidateName, candidateEmail, file);
            return ResponseEntity.ok(resume);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Upload failed: " + e.getMessage());
        }
    }

    // GET /api/resumes - Get all resumes
    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    // GET /api/resumes/{id} - Get resume by ID
    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResumeById(@PathVariable Long id) {
        Optional<Resume> resumeOpt = resumeService.getResumeById(id);
        if (resumeOpt.isPresent()) {
            return ResponseEntity.ok(resumeOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/resumes/{id} - Delete a resume
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResume(@PathVariable Long id) {
        boolean deleted = resumeService.deleteResume(id);
        if (deleted) {
            return ResponseEntity.ok("Resume deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}