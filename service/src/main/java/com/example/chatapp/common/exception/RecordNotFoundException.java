package com.example.chatapp.common.exception;

public class RecordNotFoundException extends RuntimeException{
    public static final String CODE = "RECORD_NOT_FOUND";
    public RecordNotFoundException(){
        this(CODE);
    }

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static RecordNotFoundException build(String message, Throwable cause){
        return new RecordNotFoundException(message != null ? message : CODE, cause);
    }
    public static RecordNotFoundException build(String message){
        return new RecordNotFoundException(message != null ? message : CODE);
    }
}
