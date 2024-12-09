package com.example.chatapp.features.message;

import com.example.chatapp.db.entity.MessageStatus;
import org.springframework.stereotype.Component;

@Component
public class MessageStatusTransaction {
    public MessageStatus sent(){
        return MessageStatus.SENT;
    }

    public MessageStatus read(MessageStatus oldStatus){
        if(oldStatus != MessageStatus.SENT)
            throw new MessageStatusTransactionException();
        return MessageStatus.READ;
    }
}

