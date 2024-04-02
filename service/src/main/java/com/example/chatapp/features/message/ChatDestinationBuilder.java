package com.example.chatapp.features.message;

import com.example.chatapp.db.entity.RecipientType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatDestinationBuilder {
    static String PREFIX = "/queue/msg";
    public String build(long recipientId, RecipientType recipientType){
        return PREFIX + "/" + recipientType + "/" + recipientId;
    }
}
