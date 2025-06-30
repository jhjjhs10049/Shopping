package org.zerock.ex3.review.exception;

import lombok.Data;

@Data
public class ReviewTaskException extends RuntimeException{

    private String message;
    private int code;

    public ReviewTaskException(String message, int code) {
        super(message);
        this.message=message;
        this.code=code;
    }
}
