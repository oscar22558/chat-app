package com.example.chatapp.features.recipient;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecipientService {

}
