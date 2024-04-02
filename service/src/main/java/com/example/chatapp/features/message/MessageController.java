package com.example.chatapp.features.message;

import com.example.chatapp.db.entity.RecipientType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@AllArgsConstructor
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;
    @MessageMapping("/msg")
    public void acceptMsg(MessageRequest messageRequest){
        messageService.acceptMsg(
                messageRequest.getContent(),
                messageRequest.getSendAt(),
                messageRequest.getRecipientId(),
                messageRequest.getRecipientType()
        );
    }

    @MessageMapping("/msg/read")
    public void readMsg(ReadMessageRequest request){
        messageService.readNewMsg(request.getMessageId(), request.getReadTime());
    }

    @ResponseBody
    @GetMapping("/api/chat/conversion")
    public List<MessageView> getConversion(@RequestParam Long recipientId, @RequestParam RecipientType recipientType){
        return messageService.getConversion(recipientId, recipientType);
    }
}
