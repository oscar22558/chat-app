package com.example.chatapp.features.contact;

import com.example.chatapp.features.contact.model.ContactView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactController {
    ContactService service;
    @SubscribeMapping("/user/{id}/contact")
    public List<ContactView> getContact(){
        return service.getContact();
    }
}
