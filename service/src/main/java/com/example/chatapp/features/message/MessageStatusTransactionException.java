package com.example.chatapp.features.message;

import com.example.chatapp.features.friend.model.FriendStatusTransactionException;

public class MessageStatusTransactionException extends RuntimeException {
    public static final String CODE = "MESSAGE_STATUS_TRANSACTION_EXCEPTION";
    public MessageStatusTransactionException(){
        this(CODE);
    }

    public MessageStatusTransactionException(String message) {
        super(message);
    }

    public MessageStatusTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MessageStatusTransactionException build(String message, Throwable cause){
        return new MessageStatusTransactionException(message != null ? message : CODE, cause);
    }
    public static MessageStatusTransactionException build(String message){
        return new MessageStatusTransactionException(message != null ? message : CODE);
    }
}
