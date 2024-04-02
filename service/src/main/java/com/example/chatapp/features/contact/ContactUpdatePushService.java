package com.example.chatapp.features.contact;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.model.ContactDTO;
import com.example.chatapp.redis.ContactRedisRepo;
import com.example.chatapp.redis.entity.Contact;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactUpdatePushService {

    SimpMessagingTemplate messagingTemplate;
    ContactRedisRepo contactRedisRepo;
    ContactDTOConverter mapper;

    public void push(AppUser user){
        var contacts = contactRedisRepo.findAllByUserId(user.getId());
        push(user, contacts);
    }

    public void push(AppUser user, List<Contact> contacts){
        var username = user.getUsername();
        var payload = mapper.map(contacts);

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/contact",
                payload
        );
    }

    public void pushAll(List<AppUser> users){

    }
}
