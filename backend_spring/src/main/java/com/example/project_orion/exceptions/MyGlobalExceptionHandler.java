package com.example.project_orion.exceptions;

import com.example.project_orion.payload.responses.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice // global exception interceptor
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            fieldErrors.put(fieldName, message);
        });

        response.put("status", false);
        response.put("errors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        APIResponse apiResponse = new APIResponse(e.getMessage(), false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> myHttpMessageNotReadableException(HttpMessageNotReadableException e){
        String errorMessage = "Invalid input: " + e.getMostSpecificCause().getMessage();
        String response = extractEnumErrorMessage(errorMessage);
        APIResponse apiResponse = new APIResponse(response, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    public static String extractEnumErrorMessage(String errorMessage) {

        System.out.println("Raw error message: " + errorMessage);

        // Regex pattern to capture the enum field (e.g., 'Subject' or 'Status')
        Pattern fieldPattern = Pattern.compile("Cannot deserialize value of type `com.example.project_orion.enums.(\\w+)` from");
        Matcher fieldMatcher = fieldPattern.matcher(errorMessage);

        // Extract the field name (like 'Subject', 'Status', etc.)
        String fieldName = "";
        if (fieldMatcher.find()) {
            fieldName = fieldMatcher.group(1).toLowerCase();
            System.out.println("Field Name: " + fieldName);
        } else {
            System.out.println("Field not found in error message");
        }
        Pattern pattern = Pattern.compile("Invalid input: Cannot deserialize value of type .* from String \"(.*?)\": not one of the values accepted for Enum class: \\[(.*?)\\]");
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find() && !fieldName.isEmpty()) {
            String invalidInput = matcher.group(1);
            String validEnumValues = matcher.group(2);
            return "Invalid value for " + fieldName + " '"+ invalidInput + "'. Allowed values are ["+  validEnumValues + "].";
        }
        return errorMessage;
    }

}