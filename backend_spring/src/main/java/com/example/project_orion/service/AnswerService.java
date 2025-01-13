package com.example.project_orion.service;

import com.example.project_orion.models.Submission;
import com.example.project_orion.payload.responses.AnswerResponse;
import com.example.project_orion.payload.responses.ValidationResponse;

public interface AnswerService {
    AnswerResponse getAnswer(Long questionId);

//    ValidationResponse validateAnswer(Submission submission);

    ValidationResponse validateAnswer(String username, Long questionId, Long optionId);
}

