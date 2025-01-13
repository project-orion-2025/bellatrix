package com.example.project_orion.service.impl;
import com.example.project_orion.enums.SubmissionStatus;
import com.example.project_orion.exceptions.APIException;
import com.example.project_orion.models.Question;
import com.example.project_orion.models.Submission;
import com.example.project_orion.payload.responses.AnswerResponse;
import com.example.project_orion.payload.responses.ValidationResponse;
import com.example.project_orion.repository.QuestionRepository;
import com.example.project_orion.repository.SubmissionRepository;
import com.example.project_orion.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public AnswerResponse getAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new APIException("Question with id " + questionId + " not found!"));

        Long correctOptionId = question.getAnswer() != null ? question.getAnswer().getCorrectOptionId() : null;

        return AnswerResponse.builder()
                                .questionId(questionId)
                                .correctOptionId(correctOptionId)
                                .build();
    }

    @Override
    public ValidationResponse validateAnswer(String username, Long questionId, Long submittedOptionId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> {
                    return new APIException("Question with id %d not found!".formatted(questionId));
                });
        Long correctOptionId = question.getAnswer() != null ? question.getAnswer().getCorrectOptionId() : null;
        SubmissionStatus status;
        if(correctOptionId == null){
            status = SubmissionStatus.ANSWER_DOES_NOT_EXIST;
        } else if (Objects.equals(correctOptionId, submittedOptionId)) {
            status = SubmissionStatus.CORRECT;
        }else{
            status = SubmissionStatus.INCORRECT;
        }

        Submission submission = Submission.builder()
                .questionId(questionId)
                .optionId(submittedOptionId)
                .username(username)
                .submissionStatus(status)
                .timestamp(LocalDateTime.now())
                .build();

        submissionRepository.save(submission);

        return new ValidationResponse(status);
    }
}
