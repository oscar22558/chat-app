package com.example.chatapp.features.message;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.db.repo.MessageJpaRepo;
import com.example.chatapp.features.user.UserIdentityService;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    String destination = "/queue/msg";
    @Transactional
    public void acceptMsg(String msg, Timestamp sendAt, long recipientId, RecipientType recipientType) {
        var authedUser = userIdentityService.getUser();
        // save to db
        var msgEntity = new Message();
        msgEntity.setContent(msg);
        msgEntity.setSender(authedUser);
        msgEntity.setRecipientType(recipientType);
        msgEntity.setRecipientId(recipientId);
        msgEntity.setSendAt(sendAt);
        msgEntity.setStatus(MessageStatus.SENT);
        messageJpaRepo.save(msgEntity);
        //generate payload
        var payload = getConversion(recipientId, recipientType);
        // push to sender + other users
        getUsersToNotify(recipientId, recipientType)
                .forEach(username ->
                    messagingTemplate.convertAndSendToUser(username, destination, payload)
                );
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
                        msg.getReadAt()
                )).toList();
    }

    private List<String> getUsersToNotify(long recipientId, RecipientType recipientType){
        var userId = userIdentityService.getUser().getId();
        var username = appUserJpaRepo.findById(userId)
                .orElseThrow(RecordNotFoundException::new).getUsername();
        var usernames = Lists.newArrayList(username);

        if(recipientType == RecipientType.USER){
            var recipientUsername = appUserJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new).getUsername();
            usernames.add(recipientUsername);
        }else{
            var otherUsernames = groupJpaRepo
                    .findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getMembers()
                    .stream()
                    .map(Member::getUser)
                    .map(AppUser::getUsername)
                    .toList();
            usernames.addAll(otherUsernames);
        }

        return usernames;
    }
}
