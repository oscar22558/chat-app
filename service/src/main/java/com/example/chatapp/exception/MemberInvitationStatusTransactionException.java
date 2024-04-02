package com.example.chatapp.exception;

public class MemberInvitationStatusTransactionException extends RuntimeException{
    public static final String CODE = "MEMBER_INVITATION_STATUS_TRANSACTION_EXCEPTION";
    public MemberInvitationStatusTransactionException(){
        this(CODE);
    }

    public MemberInvitationStatusTransactionException(String message) {
        super(message);
    }

    public MemberInvitationStatusTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MemberInvitationStatusTransactionException build(String message, Throwable cause){
        return new MemberInvitationStatusTransactionException(message != null ? message : CODE, cause);
    }
    public static MemberInvitationStatusTransactionException build(String message){
        return new MemberInvitationStatusTransactionException(message != null ? message : CODE);
    }
}
