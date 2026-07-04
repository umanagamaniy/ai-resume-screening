package com.uma.airesumescreening;

import com.uma.airesumescreening.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class ScreeningService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Screening analyzeResume(Long jobId, Long resumeId) throws Exception {

        // 1. Fetch job + resume from DB (throws ResourceNotFoundException if missing)
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume with id " + resumeId + " not found"));

        // 2. Build the prompt for Gemini
        String prompt = buildPrompt(job, resume);

        // 3. Call Gemini API
        String geminiResponse = callGeminiApi(prompt);

        // 4. Extract JSON portion from Gemini's text response
        String cleanedJson = extractJsonFromResponse(geminiResponse);

        // 5. Parse the JSON into a Screening object
        JsonNode aiResult = objectMapper.readTree(cleanedJson);

        Screening screening = new Screening();
        screening.setJobId(jobId);
        screening.setResumeId(resumeId);
        screening.setMatchScore(aiResult.get("matchScore").asInt());
        screening.setMatchingSkills(aiResult.get("matchingSkills").toString());
        screening.setMissingSkills(aiResult.get("missingSkills").toString());
        screening.setAiSummary(aiResult.get("summary").asText());
        screening.setInterviewQuestions(aiResult.get("interviewQuestions").toString());

        // 6. Save to DB and return
        return screeningRepository.save(screening);
    }

    private String buildPrompt(Job job, Resume resume) {
        return """
            Analyze this resume against the job description.

            Return ONLY valid JSON in this EXACT format (no markdown, no code fences, no explanation before or after):
            {
                "matchScore": <integer 0-100>,
                "matchingSkills": ["skill1", "skill2"],
                "missingSkills": ["skill3", "skill4"],
                "summary": "one paragraph analysis",
                "interviewQuestions": ["Q1", "Q2", "Q3"]
            }

            JOB DESCRIPTION:
            Title: %s
            Description: %s
            Required Skills: %s

            RESUME:
            %s
            """.formatted(
                job.getTitle(),
                job.getDescription(),
                job.getRequiredSkills(),
                resume.getExtractedText()
        );
    }

    private String callGeminiApi(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        String url = apiUrl + "?key=" + apiKey;

        return restTemplate.postForObject(url, entity, String.class);
    }

    private String extractJsonFromResponse(String geminiResponse) throws Exception {
        // Gemini's response is wrapped in {candidates:[{content:{parts:[{text: "..."}]}}]}
        JsonNode root = objectMapper.readTree(geminiResponse);
        String text = root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();

        // Sometimes Gemini wraps JSON in ```json ... ``` markdown fences. Strip them.
        text = text.replace("```json", "").replace("```", "").trim();
        return text;
    }
}