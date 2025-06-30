package org.zerock.ex3.product.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductTaskException extends RuntimeException{

    private int code;
    private String message;

    public ProductTaskException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
