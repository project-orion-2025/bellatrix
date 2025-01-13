package com.example.project_orion.service;

import com.example.project_orion.payload.responses.UserProgress;

public interface SubmissionService {
    UserProgress getUserProgress(String username);
}
