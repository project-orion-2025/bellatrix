package com.example.project_orion.service;

import com.example.project_orion.payload.Filter;
import com.example.project_orion.payload.dtos.QuestionDTO;
import com.example.project_orion.payload.responses.QuestionResponse;
import jakarta.validation.Valid;

public interface QuestionService {
    QuestionResponse getAllQuestions(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    QuestionDTO createQuestion(@Valid QuestionDTO questionDTO);

    QuestionDTO getQuestionById(Long questionId);

    QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO);

    QuestionDTO deleteQuestion(@Valid Long questionId);

    QuestionResponse fetchAllQuestions(Filter filter, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

}
