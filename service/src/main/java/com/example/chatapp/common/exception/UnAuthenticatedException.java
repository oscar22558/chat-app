package com.example.chatapp.common.exception;

public class UnAuthenticatedException extends RuntimeException {
    public static final String CODE = "UNAUTHENTICATED";
    public UnAuthenticatedException(String message) {
        super(message);
    }

    public UnAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
    public static UnAuthenticatedException build(String message, Throwable cause){
        return new UnAuthenticatedException(message != null ? message : CODE, cause);
    }
    public static UnAuthenticatedException build(String message){
        return new UnAuthenticatedException(message != null ? message : CODE);
    }
}
