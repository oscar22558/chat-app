package com.example.chatapp.features.contact;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.model.ContactDTO;
import com.example.chatapp.features.user.UserIdentityService;
import com.example.chatapp.redis.ContactRedisRepo;
import com.example.chatapp.redis.entity.Contact;
import com.example.chatapp.redis.entity.ContactKeyBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ContactService {
    UserIdentityService userIdentityService;
    ContactDTOConverter mapper;
    ContactRedisRepo contactRedisRepo;
    RedisTemplate<String, Object> redisTemplate;
    ContactUpdatePushService contactUpdatePushService;
    Logger logger = LoggerFactory.getLogger(ContactService.class);

    public List<ContactDTO> getUserContact(){
        var authedUser = userIdentityService.getUser();
        var contacts = contactRedisRepo.findAllByUserId(authedUser.getId());
        return mapper.map(contacts);
    }

    public void addContact(List<AppUser> users, Group group){
        var recipientId = group.getId();
        var recipientType = RecipientType.GROUP;

        List<Contact> contacts = users
                .stream()
                .map(user -> {
                    var newContact = new Contact();
                    newContact.setRecipientId(recipientId);
                    newContact.setRecipientType(recipientType);
                    newContact.setUpdatedAt(AppTimestamp.newInstance());
                    newContact.setUserId(user.getId());
                    return newContact;
                }).toList();
        Iterable<Contact> cachedContacts = contactRedisRepo.saveAll(contacts);

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
        List<Contact> contacts = contactRedisRepo.findAllByUserIdAndRecipientIdAndRecipientType(
                user.getId(), group.getId(), RecipientType.GROUP
        );
        contactRedisRepo.deleteAll(contacts);
    }

    public void markNewUsrMsgAsRead(AppUser sender, AppUser recipient, int msgCount){
        logger.info("markNewMsgAsRead: {}, {}", sender.getUsername(), recipient.getUsername());
        if(sender.getId().equals(recipient.getId())) return;
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                List<Object> result = new ArrayList<>();
                try{
                    var contact = getUserContact(recipient, sender);
                    operations.multi();
                    decreaseNewMessageCount(contact.getId(), msgCount);
                    increaseReadMsgCount(contact.getId(), msgCount);
                    result = operations.exec();
                } catch (Exception e) {
                    try {
                        operations.discard();
                    } catch (Exception e2) {
                        logger.error("Error on discard for markNewMsgAsRead: ", e2);
                    }
                    logger.error("Error on exec for markNewMsgAsRead: ",  e);
                }
                return result;
            }
        });
//        contactUpdatePushService.push(recipient);
    }

    public void markNewGrpMsgAsRead(AppUser sender, Group recipient, int msgCount){
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                List<Object> result = new ArrayList<>();
                try{
                    var contact = getGrpContact(sender, recipient);
                    operations.multi();
                    decreaseNewMessageCount(contact.getId(), msgCount);
                    increaseReadMsgCount(contact.getId(), msgCount);
                    result = operations.exec();
                } catch (Exception e) {
                    try {
                        operations.discard();
                    } catch (Exception e2) {
                        logger.error("Error on discard for markNewGrpMsgAsRead: ", e2);
                    }
                    logger.error("Error on exec for markNewGrpMsgAsRead: ",  e);
                }
                return result;
            }
        });
        //contactUpdatePushService.push(sender);
    }

    public void pushNewMsgNotification(AppUser sender, AppUser recipient){
        if(sender.getId().equals(recipient.getId())) return;
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                List<Object> result = new ArrayList<>();
                try{
                    var recipientContact = getUserContact(recipient, sender);
                    operations.multi();
                    increaseNewMessageCount(recipientContact.getId());
                    increaseMsgCount(recipientContact.getId());
                    result = operations.exec();
                } catch (Exception e) {
                    try {
                        operations.discard();
                    } catch (Exception e2) {
                        logger.error("Error on discard for pushNewMsgNotification: ", e2);
                    }
                    logger.error("Error on exec for pushNewMsgNotification: ",  e);
                }
                return result;
            }
        });
        contactUpdatePushService.push(recipient);
    }

    public void pushNewGrpMsgNotification(AppUser user, Group recipientGroup){

        redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                List<Object> result = new ArrayList<>();
                try{
                    var recipientContact = getGrpContact(user, recipientGroup);
                    operations.multi();
                    increaseNewMessageCount(recipientContact.getId());
                    increaseMsgCount(recipientContact.getId());
                    result = operations.exec();
                } catch (Exception e) {
                    try {
                        operations.discard();
                    } catch (Exception e2) {
                        logger.error("Error on discard for pushNewGrpMsgNotification: ", e2);
                    }
                    logger.error("Error on exec for pushNewGrpMsgNotification: ",  e);
                }
                return result;
            }
        });
        contactUpdatePushService.push(user);
    }

    public Contact getUserContact(AppUser sender, AppUser recipient){
        Stream<Contact> contacts = contactRedisRepo
                .findAllByUserIdAndRecipientIdAndRecipientType(sender.getId(), recipient.getId(), RecipientType.USER)
                .stream();
        return contacts.findFirst()
                .orElseThrow(RecordNotFoundException::new);
    }

    public Contact getGrpContact(AppUser user, Group recipientGrp){
        logger.info("{},{},{}", user.getId(), recipientGrp.getId(), RecipientType.GROUP);

        return  contactRedisRepo
                .findAllByUserIdAndRecipientIdAndRecipientType(user.getId(), recipientGrp.getId(), RecipientType.GROUP)
                .stream()
                .findFirst()
                .orElseThrow(RecordNotFoundException::new);
    }

    private boolean isContactExist(AppUser sender, AppUser recipient){
        return contactRedisRepo.existsAllByUserIdAndRecipientIdAndRecipientType(sender.getId(), recipient.getId(), RecipientType.USER);
    }

    private boolean isContactExist(AppUser sender, Group group){
        return contactRedisRepo.existsAllByUserIdAndRecipientIdAndRecipientType(sender.getId(), group.getId(), RecipientType.GROUP);
    }

    private void increaseNewMessageCount(String contactId){
        var redisKey = ContactKeyBuilder.build(contactId);
        redisTemplate.opsForHash().increment(redisKey, "newMsgCount", 1);
    }

    private void increaseMsgCount(String contactId){
        var redisKey = ContactKeyBuilder.build(contactId);
        redisTemplate.opsForHash().increment(redisKey, "msgCount", 1);
    }

    private void increaseReadMsgCount(String contactId, int msgCount){
        var redisKey = ContactKeyBuilder.build(contactId);
        redisTemplate.opsForHash().increment(redisKey, "readMsgCount", msgCount);
    }

    private void decreaseNewMessageCount(String contactId, int msgCount){
        var redisKey = ContactKeyBuilder.build(contactId);
        redisTemplate.opsForHash().increment(redisKey, "newMsgCount", -msgCount);
    }
}
