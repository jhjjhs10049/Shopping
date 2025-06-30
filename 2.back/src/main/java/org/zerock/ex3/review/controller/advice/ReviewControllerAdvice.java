package org.zerock.ex3.review.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.ex3.review.exception.ReviewTaskException;

import java.util.Map;

@RestControllerAdvice
public class ReviewControllerAdvice {

    @ExceptionHandler(ReviewTaskException.class)
    public ResponseEntity<Map<String, String>>
    handleReviewTaskException(ReviewTaskException exception){

        int status = exception.getCode();
        String message = exception.getMessage();

        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
