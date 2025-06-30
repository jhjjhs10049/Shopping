package org.zerock.ex3.product.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.ex3.product.exception.ProductTaskException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class ProductControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        log.error("handleMethodArgumentNotValidException............");
        log.error(e.getMessage());

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String errorMessage = errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }

    @ExceptionHandler(ProductTaskException.class)
    public ResponseEntity<Map<String, String>>
    handleProductTaskException(ProductTaskException e) {
        log.error("ProductTaskException........");
        log.error(e.getClass().getName());
        log.error(e.getMessage());

        int status = e.getCode();

        return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
    }
}
