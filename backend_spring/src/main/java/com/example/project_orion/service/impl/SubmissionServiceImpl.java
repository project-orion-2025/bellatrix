package com.example.project_orion.service.impl;

import com.example.project_orion.enums.Difficulty;
import com.example.project_orion.enums.SubmissionStatus;
import com.example.project_orion.models.Question;
import com.example.project_orion.models.Submission;
import com.example.project_orion.repository.QuestionRepository;
import com.example.project_orion.payload.responses.UserProgress;
import com.example.project_orion.repository.SubmissionRepository;
import com.example.project_orion.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SubmissionRepository submissionRepository;

    @Override
    public UserProgress getUserProgress(String username) {
        // Fetch submissions for the user with successful status
        List<Submission> submissions = submissionRepository.findByUsernameAndSubmissionStatus(username, SubmissionStatus.CORRECT);

        // Extract unique question IDs
        Set<Long> uniqueQuestionIds = submissions.stream()
                .map(Submission::getQuestionId)
                .collect(Collectors.toSet());

        // Fetch questions based on unique IDs
        List<Question> questions = questionRepository.findAllById(uniqueQuestionIds);

        // Count questions by difficulty
        Map<Difficulty, Long> difficultyCounts = questions.stream()
                .collect(Collectors.groupingBy(Question::getDifficulty, Collectors.counting()));

        // Populate UserProgress DTO
        return new UserProgress(
                (long) difficultyCounts.getOrDefault(Difficulty.EASY, 0L).intValue(),
                (long) difficultyCounts.getOrDefault(Difficulty.MEDIUM, 0L).intValue(),
                (long) difficultyCounts.getOrDefault(Difficulty.HARD, 0L).intValue()
        );
    }
}
