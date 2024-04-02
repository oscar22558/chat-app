package com.example.chatapp.features.message;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MessageJpaRepo;
import com.example.chatapp.features.contact.ContactService;
import com.example.chatapp.features.user.UserIdentityService;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MessageService {
    MessageJpaRepo messageJpaRepo;
    AppUserJpaRepo appUserJpaRepo;
    GroupJpaRepo groupJpaRepo;
    SimpMessagingTemplate messagingTemplate;
    UserIdentityService userIdentityService;
    ContactService contactService;
    MessageStatusTransaction messageStatusTransaction;
    String destination = "/queue/msg";
    @Transactional
    public void acceptMsg(String msg, Timestamp sendAt, long recipientId, RecipientType recipientType) {
        var authedUser = userIdentityService.getUser();
        var msgEntity = new Message();
        msgEntity.setContent(msg);
        msgEntity.setSender(authedUser);
        msgEntity.setRecipientType(recipientType);
        msgEntity.setRecipientId(recipientId);
        msgEntity.setSendAt(sendAt);
        msgEntity.setStatus(messageStatusTransaction.sent());
        messageJpaRepo.save(msgEntity);

        var payload = getConversion(recipientId, recipientType);
        getUsersToNotify(recipientId, recipientType)
                .forEach(user ->{
                    messagingTemplate.convertAndSendToUser(user.getUsername(), destination, payload);
                    contactService.pushNewMsgNotification(authedUser, user);
                });
    }

    public List<MessageView> getConversion(long receiverId, RecipientType recipientType){
        var userId = userIdentityService.getUser().getId();
        List<Message> sortedMsg;
        if(recipientType == RecipientType.USER){
            var sentMsg = appUserJpaRepo.findById(userId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getSentMessages()
                    .stream()
                    .filter(msg -> msg.getRecipientId().equals(receiverId))
                    .toList();
            var receivedMsg = appUserJpaRepo.findById(receiverId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getSentMessages()
                    .stream()
                    .filter(msg -> msg.getRecipientId().equals(userId) && msg.getRecipientType() == RecipientType.USER)
                    .toList();
            sortedMsg = Lists.newArrayList(sentMsg);
            sortedMsg.addAll(receivedMsg);
            sortedMsg.sort((o1, o2) -> (int)(o2.getSendAt().getTime() - o1.getSendAt().getTime()));
        }else{
            sortedMsg = messageJpaRepo.findMessagesByRecipientIdAndAndRecipientType(receiverId, recipientType);
        }

        return sortedMsg
                .stream()
                .map(msg -> new MessageView(
                        msg.getSender().getId(),
                        msg.getSender().getUsername(),
                        msg.getContent(),
                        msg.getSendAt(),
                        msg.getReadAt(),
                        msg.getStatus()
                )).toList();
    }

    public void readNewMsg(long msgId, Timestamp readTime){
        var authedUser = userIdentityService.getUser();
        var msg = messageJpaRepo.findById(msgId)
                        .orElseThrow(RecordNotFoundException::new);
        var newStatus = messageStatusTransaction.read(msg.getStatus());
        long receiverId = msg.getRecipientId();
        RecipientType recipientType = msg.getRecipientType();
        msg.setReadAt(readTime);
        msg.setStatus(newStatus);
        messageJpaRepo.save(msg);

        if(recipientType == RecipientType.USER){
            var group = groupJpaRepo.findById(receiverId)
                    .orElseThrow(RecordNotFoundException::new);
            contactService.markNewMsgAsRead(authedUser, group);
        }else{
            var recipient = appUserJpaRepo.findById(receiverId)
                    .orElseThrow(RecordNotFoundException::new);
            contactService.markNewMsgAsRead(authedUser, recipient);
        }
    }

    private List<AppUser> getUsersToNotify(long recipientId, RecipientType recipientType){
        var userId = userIdentityService.getUser().getId();
        var user = appUserJpaRepo.findById(userId)
                .orElseThrow(RecordNotFoundException::new);
        var users = Lists.newArrayList(user);

        if(recipientType == RecipientType.USER){
            var recipient = appUserJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new);
            users.add(recipient);
        }else{
            var otherUsers = groupJpaRepo
                    .findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getMembers()
                    .stream()
                    .map(Member::getUser)
                    .toList();
            users.addAll(otherUsers);
        }

        return users;
    }
}
