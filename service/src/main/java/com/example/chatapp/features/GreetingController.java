package com.example.chatapp.features;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    public void greeting(Principal principal, HelloMessage message) throws  Exception {
        Greeting greeting = new Greeting();
        greeting.setContent("Hello!");
        LoggerFactory.getLogger(GreetingController.class).info("Someone said hello!");
        messagingTemplate.convertAndSendToUser(message.getToUser(), "/queue/reply", greeting);
    }

}