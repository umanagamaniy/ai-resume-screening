package com.uma.airesumescreening;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

    @GetMapping("/ask")
    public String askAI(@RequestParam String question) {
        return geminiService.askGemini(question);
    }
}