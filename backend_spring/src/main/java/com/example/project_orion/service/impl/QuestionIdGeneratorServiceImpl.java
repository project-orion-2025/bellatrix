package com.example.project_orion.service.impl;

import com.example.project_orion.repository.QuestionRepository;
import com.example.project_orion.service.QuestionIdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionIdGeneratorServiceImpl implements QuestionIdGeneratorService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public synchronized Long generateNextId() {
        Long lastId = questionRepository.findMaxId();
        return (lastId != null) ? lastId + 1 : 1L;
    }
}