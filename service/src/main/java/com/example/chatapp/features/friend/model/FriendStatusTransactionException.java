package com.example.chatapp.features.friend.model;

public class FriendStatusTransactionException extends RuntimeException{
    public static final String CODE = "FRIEND_STATUS_TRANSACTION_EXCEPTION";
    public FriendStatusTransactionException(){
        this(CODE);
    }

    public FriendStatusTransactionException(String message) {
        super(message);
    }

    public FriendStatusTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static FriendStatusTransactionException build(String message, Throwable cause){
        return new FriendStatusTransactionException(message != null ? message : CODE, cause);
    }
    public static FriendStatusTransactionException build(String message){
        return new FriendStatusTransactionException(message != null ? message : CODE);
    }
}
