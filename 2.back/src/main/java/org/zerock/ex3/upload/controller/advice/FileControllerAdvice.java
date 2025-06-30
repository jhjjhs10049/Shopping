package org.zerock.ex3.upload.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.zerock.ex3.upload.exception.UploadNotSupportedException;

import java.util.Map;

@RestControllerAdvice
public class FileControllerAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?>
    handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return ResponseEntity.badRequest().body( Map.of("error", "File too large"));
    }

    @ExceptionHandler(UploadNotSupportedException.class)
    public ResponseEntity<Map<String, String>>
    handleUploadNotSupportedException(UploadNotSupportedException exception)
    {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }
}
