package com.example.chatapp.features.message;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.features.user.model.SpringUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageController {
    ObjectMapper objectMapper;
    MessageService messageService;

    @MessageMapping("/msg")
    public void acceptMsg(@AuthenticationPrincipal SpringUser SpringUser, MessageRequest messageRequest) throws JsonProcessingException {
        AppUser appUser = (SpringUser).getUser();
        log.info("[{}]Accepting message: {}", appUser.getUsername(), objectMapper.writeValueAsString(messageRequest));

        messageService.acceptMsg(
                appUser,
                messageRequest.getContent(),
                messageRequest.getSendAt(),
                messageRequest.getRecipientId(),
                messageRequest.getRecipientType()
        );

        messageService.notifyUsersOnNewMsg(
                appUser,
                messageRequest.getRecipientId(),
                messageRequest.getRecipientType()
        );

    }

    @MessageMapping("/msg/read")
    public void readMsg(@AuthenticationPrincipal SpringUser SpringUser, ReadMessageRequest request) throws JsonProcessingException {
        AppUser appUser = (SpringUser).getUser();
        log.info("[{}]Reading message: {}", appUser.getUsername(), objectMapper.writeValueAsString(request));
        messageService.readNewMsgList(appUser, request.getMessageIds(), request.getReadTime());
    }

    @ResponseBody
    @GetMapping("/api/chat/conversion")
    public List<MessageDTO> getConversion(@AuthenticationPrincipal SpringUser SpringUser, @RequestParam Long recipientId, @RequestParam RecipientType recipientType) throws JsonProcessingException {
        AppUser appUser = (SpringUser).getUser();
        log.info("[{}]/api/chat/conversion: recipientId:{}, body: {}", appUser.getUsername(), recipientId, objectMapper.writeValueAsString(recipientType));
        return messageService.getConversion(appUser, recipientId, recipientType);
    }
}
