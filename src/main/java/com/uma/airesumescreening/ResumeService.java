package com.uma.airesumescreening;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Upload a resume file + save metadata to DB
    public Resume uploadResume(String candidateName, String candidateEmail, MultipartFile file) throws IOException {

        // 1. Validate file
        if (file.isEmpty()) {
            throw new IOException("Cannot upload empty file");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IOException("Only PDF files are allowed");
        }

        // 2. Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Generate a unique filename to avoid collisions
        // e.g. "550e8400-e29b-41d4-a716-446655440000_uma_resume.pdf"
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path targetPath = uploadPath.resolve(uniqueFilename);

        // 4. Save file to disk
        file.transferTo(targetPath.toFile());

        // 5. Save metadata to database
        Resume resume = new Resume();
        resume.setCandidateName(candidateName);
        resume.setCandidateEmail(candidateEmail);
        resume.setFileName(originalFilename);
        resume.setFilePath(targetPath.toString());
        resume.setFileSize(file.getSize());

        return resumeRepository.save(resume);
    }

    // Get all resumes
    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    // Get resume by ID
    public Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }

    // Delete a resume (both DB row + physical file)
    public boolean deleteResume(Long id) {
        Optional<Resume> resumeOpt = resumeRepository.findById(id);
        if (resumeOpt.isEmpty()) {
            return false;
        }

        Resume resume = resumeOpt.get();

        // Delete file from disk (if it exists)
        File file = new File(resume.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // Delete row from database
        resumeRepository.deleteById(id);
        return true;
    }
}