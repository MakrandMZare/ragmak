package com.example.ragmak.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ragmak.model.AskRequest;
import com.example.ragmak.service.RagService;

@RestController
public class AskController {

    private final RagService ragService;

    public AskController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody AskRequest request) {
      if (request.getQuestion() == null || request.getQuestion().isBlank()) {
          return "{\"Error\": \"Question cannot be empty.\"}";
    }
    return ragService.answer(request.getQuestion());
    } 
}
