package com.example.project_orion.controller;

import com.example.project_orion.config.AppConstants;
import com.example.project_orion.payload.Filter;
import com.example.project_orion.payload.dtos.QuestionDTO;
import com.example.project_orion.payload.responses.QuestionResponse;
import com.example.project_orion.service.QuestionService;
import com.example.project_orion.service.validation.CreateQuestion;
import com.example.project_orion.service.validation.UpdateQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.groups.Default;

@RestController
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/public/questions")
    public ResponseEntity<QuestionResponse> getAllQuestions(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_QUESTIONS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        /*
        TODO: API crashing if the pagination details were wrong
        */
        QuestionResponse questionResponse = questionService.getAllQuestions(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(questionResponse, HttpStatus.OK);
    }

    @PostMapping("/author/questions")
    public ResponseEntity<QuestionDTO> createQuestion(
            @Validated({CreateQuestion.class, Default.class})
            @RequestBody QuestionDTO questionDTO,
            @AuthenticationPrincipal UserDetails userDetails){
        QuestionDTO savedQuestionDTO = questionService.createQuestion(userDetails.getUsername(), questionDTO);
        return new ResponseEntity<>(savedQuestionDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/question/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long questionId){
        QuestionDTO questionDTO = questionService.getQuestionById(questionId);
        return new ResponseEntity<>(questionDTO, HttpStatus.OK);
    }

    @PutMapping("/author/question/{questionId}")
    public ResponseEntity<QuestionDTO> updateCategory(@Validated({UpdateQuestion.class, Default.class}) @RequestBody QuestionDTO questionDTO, @PathVariable Long questionId){
        QuestionDTO savedQuestionDTO = questionService.updateQuestion(questionId, questionDTO);
        return new ResponseEntity<>(savedQuestionDTO, HttpStatus.OK);
    }

    @DeleteMapping("/author/question/{questionId}")
    public ResponseEntity<QuestionDTO> deleteCategory(@PathVariable Long questionId){
        QuestionDTO savedQuestionDTO = questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(savedQuestionDTO, HttpStatus.OK);
    }

    @PostMapping("/public/question/search")
    public ResponseEntity<QuestionResponse> searchQuestionsByTitle(
            @RequestBody Filter filter,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_QUESTIONS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
            ) {
        QuestionResponse questions = questionService.fetchAllQuestions(filter, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }
}
