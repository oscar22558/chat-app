package com.example.chatapp.common.exception;

public class UnAuthorizedException extends RuntimeException{
    public static final String CODE = "UNAUTHORIZED";
    public UnAuthorizedException(){
        this(CODE);
    }

    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UnAuthorizedException build(String message, Throwable cause){
        return new UnAuthorizedException(message != null ? message : CODE, cause);
    }
    public static UnAuthorizedException build(String message){
        return new UnAuthorizedException(message != null ? message : CODE);
    }
}
