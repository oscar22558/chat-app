package com.example.chatapp.features.contact;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.features.contact.model.ContactView;
import com.example.chatapp.features.user.UserIdentityService;
import com.example.chatapp.redis.ContactRedisRepo;
import com.example.chatapp.redis.entity.Contact;
import com.example.chatapp.redis.entity.ContactKeyBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ContactService {
    UserIdentityService userIdentityService;
    ContactViewMapper mapper;
    ContactRedisRepo contactRedisRepo;
    RedisTemplate<String, Object> redisTemplate;
    ContactUpdatePushService contactUpdatePushService;
    Logger logger = LoggerFactory.getLogger(ContactService.class);

    public List<ContactView> getContact(){
        var authedUser = userIdentityService.getUser();
        var contacts = contactRedisRepo.findAllByUserId(authedUser.getId());
        return mapper.map(contacts);
    }

    public void addContact(AppUser user, Group group){
        var recipientId = group.getId();
        var recipientType = RecipientType.GROUP;
        if(isContactExist(user, group))
            return;

        var newContact = new Contact();
        newContact.setRecipientId(recipientId);
        newContact.setRecipientType(recipientType);
        newContact.setUpdatedAt(AppTimestamp.newInstance());
        newContact.setUserId(user.getId());
        contactRedisRepo.save(newContact);
        contactUpdatePushService.push(user);
    }

    public void addContact(AppUser user, AppUser recipient){
        if(!isContactExist(user, recipient)){
            var userContact = new Contact();
            userContact.setUpdatedAt(AppTimestamp.newInstance());
            userContact.setRecipientId(recipient.getId());
            userContact.setRecipientType(RecipientType.USER);
            userContact.setUserId(user.getId());
            contactRedisRepo.save(userContact);
        }

        if(!isContactExist(recipient, user)){
            var recipientContact = new Contact();
            recipientContact.setUpdatedAt(AppTimestamp.newInstance());
            recipientContact.setRecipientId(user.getId());
            recipientContact.setRecipientType(RecipientType.USER);
            recipientContact.setUserId(recipient.getId());
            contactRedisRepo.save(recipientContact);
        }

        contactUpdatePushService.push(user);
        contactUpdatePushService.push(recipient);
    }

    public void removeContact(AppUser user, Group group){
        contactRedisRepo.deleteAllByUserIdAndRecipientIdAndAndRecipientType(
                user.getId(), group.getId(), RecipientType.GROUP
        );
    }

    public void markNewMsgAsRead(AppUser sender, AppUser recipient){
        logger.info("markNewMsgAsRead: {}, {}", sender.getUsername(), recipient.getUsername());
        if(sender.getId().equals(recipient.getId())) return;
        var contact = getContact(sender, recipient);
        decreaseNewMessageCount(contact.getId());
        contactUpdatePushService.push(recipient);
    }

    public void markNewMsgAsRead(AppUser sender, Group recipient){
        var contact = getContact(sender, recipient);
        decreaseNewMessageCount(contact.getId());
        contactUpdatePushService.push(sender);
    }

    public void pushNewMsgNotification(AppUser sender, AppUser recipient){
        if(sender.getId().equals(recipient.getId())) return;
        var recipientContact = getContact(sender, recipient);
        increaseNewMessageCount(recipientContact.getId());
        contactUpdatePushService.push(recipient);
    }

    public Contact getContact(AppUser sender, AppUser recipient){
        return contactRedisRepo
                .findAllByUserIdAndRecipientIdAndAndRecipientType(sender.getId(), recipient.getId(), RecipientType.USER)
                .stream()
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);
    }

    public Contact getContact(AppUser sender, Group recipient){
        return  contactRedisRepo
                .findAllByUserIdAndRecipientIdAndAndRecipientType(sender.getId(), recipient.getId(), RecipientType.GROUP)
                .stream()
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);
    }

    private boolean isContactExist(AppUser sender, AppUser recipient){
        return contactRedisRepo.existsAllByUserIdAndRecipientIdAndAndRecipientType(sender.getId(), recipient.getId(), RecipientType.USER);
    }

    private boolean isContactExist(AppUser sender, Group group){
        return contactRedisRepo.existsAllByUserIdAndRecipientIdAndAndRecipientType(sender.getId(), group.getId(), RecipientType.USER);
    }

    private void increaseNewMessageCount(String contactId){
        var redisKey = ContactKeyBuilder.build(contactId);
        redisTemplate.opsForHash().increment(redisKey, "newMsgCount", 1);
    }

    private void decreaseNewMessageCount(String contactId){
        var redisKey = ContactKeyBuilder.build(contactId);
        redisTemplate.opsForHash().increment(redisKey, "newMsgCount", -1);
    }
}
