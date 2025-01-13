package com.example.project_orion.controller;

import com.example.project_orion.payload.responses.AnswerResponse;
import com.example.project_orion.payload.responses.ValidationResponse;
import com.example.project_orion.payload.responses.UserProgress;
import com.example.project_orion.service.AnswerService;
import com.example.project_orion.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/answer/{questionId}")
    public ResponseEntity<AnswerResponse> getAnswerId(@PathVariable Long questionId){
        AnswerResponse answerResponse =  answerService.getAnswer(questionId);
        return new ResponseEntity<>(answerResponse, HttpStatus.OK);
    }

    @PostMapping("/submit-answer")
    public ResponseEntity<ValidationResponse> validateAnswer(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long questionId,
            @RequestParam Long optionId){
        ValidationResponse validationResponse = answerService.validateAnswer(userDetails.getUsername(), questionId, optionId);
        return new ResponseEntity<>(validationResponse, HttpStatus.OK);
    }

    @GetMapping("/get-user-progress")
    public ResponseEntity<UserProgress> getUserProgress(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserProgress userProgress = submissionService.getUserProgress(userDetails.getUsername());
        return new ResponseEntity<>(userProgress, HttpStatus.OK);
    }
}
