package org.zerock.ex3.upload.exception;

public class UploadNotSupportedException extends RuntimeException{

    public UploadNotSupportedException(String message) {
        super(message);
    }
}
