package com.example.chatapp.features.recipient;

import com.example.chatapp.features.recipient.model.RecipientView;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RecipientController {
    @SubscribeMapping("/user/{id}/recipients")
    public List<RecipientView> getRecipients(@DestinationVariable Long id) throws Exception {
        //return a list of recipient(users and groups) who received at least one message from user +
        // a list of senders who sent at least one message to user
        return List.of();
    }

}
