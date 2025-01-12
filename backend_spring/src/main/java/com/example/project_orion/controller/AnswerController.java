package com.example.project_orion.controller;

import com.example.project_orion.models.Submission;
import com.example.project_orion.payload.responses.AnswerResponse;
import com.example.project_orion.payload.responses.ValidationResponse;
import com.example.project_orion.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/answer/{questionId}")
    public ResponseEntity<AnswerResponse> getAnswerId(@PathVariable Long questionId){
        AnswerResponse answerResponse =  answerService.getAnswer(questionId);
        return new ResponseEntity<>(answerResponse, HttpStatus.OK);
    }

    @PostMapping("/validate/answer")
    public ResponseEntity<ValidationResponse> validateAnswer(@Valid @RequestBody Submission submission){
        ValidationResponse validationResponse = answerService.validateAnswer(submission);
        return new ResponseEntity<>(validationResponse, HttpStatus.OK);
    }
}
