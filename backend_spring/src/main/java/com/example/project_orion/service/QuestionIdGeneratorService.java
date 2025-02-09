package com.example.project_orion.service;

import com.example.project_orion.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionIdGeneratorService {

    @Autowired
    private QuestionRepository questionRepository;

    public synchronized Long generateNextId() {
        Long lastId = questionRepository.findMaxId();
        return (lastId != null) ? lastId + 1 : 1L;
    }
}