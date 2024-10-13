package com.example.chatapp.features.contact;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.redis.ContactRedisRepo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactUpdatePushService {

    SimpMessagingTemplate messagingTemplate;
    ContactRedisRepo contactRedisRepo;
    ContactViewMapper mapper;

    public void push(AppUser user){
        var contacts = contactRedisRepo.findAllByUserId(user.getId());
        var username = user.getUsername();
        var payload = mapper.map(contacts);

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/contact",
                payload
        );
    }
}
