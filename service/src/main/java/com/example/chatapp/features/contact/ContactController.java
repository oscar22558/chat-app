package com.example.chatapp.features.contact;

import com.example.chatapp.features.contact.model.ContactView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactController {
    ContactService service;
    @GetMapping("/contact")
    public List<ContactView> getContact(){
        return service.getContact();
    }
}
