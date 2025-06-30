package org.zerock.ex3.cart.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.ex3.cart.exception.CartTaskException;

import java.util.Map;

@RestControllerAdvice
public class CartControllerAdvice {

    @ExceptionHandler(CartTaskException.class)
    public ResponseEntity<Map<String, String>>
    handleCartTaskException(CartTaskException e) {

        int status = e.getStatus();
        String message = e.getMessage();

        return ResponseEntity.status(status).body(Map.of("error", message));

    }
}
