package com.example.chatapp.features.message;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.db.entity.*;
import com.example.chatapp.db.repo.*;
import com.example.chatapp.features.contact.ContactService;
import com.example.chatapp.features.contact.ContactUpdatePushService;
import com.example.chatapp.features.user.UserIdentityService;
import com.example.chatapp.redis.ContactRedisRepo;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class MessageService {
    MessageJpaRepo messageJpaRepo;
    AppUserJpaRepo appUserJpaRepo;
    GroupJpaRepo groupJpaRepo;
    SimpMessagingTemplate messagingTemplate;
    UserIdentityService userIdentityService;
    ContactService contactService;
    MessageStatusTransaction messageStatusTransaction;
    ChatDestinationBuilder chatDestinationBuilder;
    MemberJpaRepo memberJpaRepo;
    GrpMsgReadStatusRepo grpMsgReadStatusRepo;
    private final ContactRedisRepo contactRedisRepo;
    ContactUpdatePushService contactUpdatePushService;
    private final HandlerMapping stompWebSocketHandlerMapping;


    public void acceptMsg(AppUser sender, String msg, Timestamp sendAt, long recipientId, RecipientType recipientType) {
        var msgEntity = new Message();
        msgEntity.setContent(msg);
        msgEntity.setSender(sender);
        msgEntity.setRecipientType(recipientType);
        msgEntity.setRecipientId(recipientId);
        msgEntity.setSendAt(sendAt);
        msgEntity.setStatus(messageStatusTransaction.sent());
        var newMsgEntity = messageJpaRepo.save(msgEntity);

        if(recipientType == RecipientType.GROUP) {
            var grp = groupJpaRepo.findById(recipientId).orElseThrow(RecordNotFoundException::new);
            var members = memberJpaRepo.findByGroupId(recipientId);
            var grpMsgStatuses = members.stream()
                    .map(member -> {
                        var status = new GrpMsgReadStatus();
                        status.setCreateAt(AppTimestamp.newInstance());
                        status.setGrpUsr(member.getUser());
                        status.setGrp(grp);
                        status.setGrpMsg(newMsgEntity);
                        return status;
                    })
                    .toList();
            grpMsgReadStatusRepo.saveAll(grpMsgStatuses);
        }
    }

    public List<MessageDTO> getConversion(AppUser sender, long receiverId, RecipientType recipientType){
        var userId = sender.getId();
        List<Message> sortedMsgList;
        if(recipientType == RecipientType.USER){
//            var sentMsg = appUserJpaRepo.findById(userId)
//                    .orElseThrow(RecordNotFoundException::new)
//                    .getSentMessages()
//                    .stream()
//                    .filter(msg -> msg.getRecipientId().equals(receiverId))
//                    .toList();
            List<Message> sentMsg = messageJpaRepo
                    .findBySenderAndRecipientTypeAndRecipientId(sender,  RecipientType.USER, receiverId);
//            var receivedMsg = appUserJpaRepo.findById(receiverId)
//                    .orElseThrow(RecordNotFoundException::new)
//                    .getSentMessages()
//                    .stream()
//                    .filter(msg -> msg.getRecipientId().equals(userId) && msg.getRecipientType() == RecipientType.USER)
//                    .toList();
            List<Message> receivedMsg = messageJpaRepo.findByRecipientIdAndRecipientType(userId, RecipientType.USER);
            sortedMsgList = Lists.newArrayList(sentMsg);
            sortedMsgList.addAll(receivedMsg);
        }else{
            sortedMsgList = messageJpaRepo.findByRecipientIdAndRecipientType(receiverId, recipientType);
        }
        sortedMsgList.sort((o1, o2) -> (int)(o2.getSendAt().getTime() - o1.getSendAt().getTime()));

        return sortedMsgList
                .stream()
                .map(msg -> {
                    var msgDTO = new MessageDTO(
                            msg.getId(),
                            msg.getSender().getId(),
                            msg.getSender().getUsername(),
                            msg.getContent(),
                            msg.getSendAt(),
                            msg.getReadAt(),
                            msg.getStatus()
                    );

                    // TODO: n+1 problem
                    var grpMsgReadStatues = grpMsgReadStatusRepo.findByGrpMsgIdAndGrpUsrId(msg.getId(), userId);
                    if(!grpMsgReadStatues.isEmpty()){
                        msgDTO.setReadAt(grpMsgReadStatues.get(0).getReadAt());
                    }
                    return msgDTO;
                }).toList();
    }

    public void readNewMsgList(AppUser authUser, List<Long> msgIds, Timestamp readTime){
        var msgList = messageJpaRepo.findByIdIn(msgIds);
        var grpMsgList = msgList.stream()
                .filter(msg -> msg.getRecipientType().equals(RecipientType.GROUP) && !msg.getSender().getId().equals(authUser.getId()))
                .toList();
        if(!grpMsgList.isEmpty()){
            //group
            List<Long> grpIds = grpMsgList.stream().map(msg -> msg.getRecipientId()).toList();
            Map<Long, Group> groups = groupJpaRepo.findByIdIn(grpIds).stream().collect(Collectors.toMap(Group::getId, grp -> grp));
            Map<Long, Integer> grpMsgCount = new HashMap<>();
            List<GrpMsgReadStatus> statusList = grpMsgList.stream().map(msg -> {
                var status = grpMsgReadStatusRepo.findByGrpMsgIdAndGrpUsrId(msg.getId(), authUser.getId()).get(0);
                status.setReadAt(readTime);
                return status;
            }).toList();
            grpMsgReadStatusRepo.saveAll(statusList);

            grpMsgList.forEach(msg -> {
                Group contactGrp = groups.getOrDefault(msg.getRecipientId(), null);
                if(contactGrp != null){
                    int count = grpMsgCount.getOrDefault(msg.getRecipientId(), 0) + 1;
                    grpMsgCount.put(msg.getRecipientId(), count);
                }
            });

            grpMsgCount.forEach((key, count) -> {
                var grp = groups.getOrDefault(key, null);
                var msgCount = grpMsgCount.getOrDefault(key, 0);
                if(grp != null){
                    contactService.markNewGrpMsgAsRead(authUser, grp, msgCount);
                }
            });

            contactUpdatePushService.push(authUser);

            Long grpId = grpMsgList.getFirst().getRecipientId();
            List<MessageDTO> payload = getConversion(authUser, grpId, RecipientType.GROUP);
            String senderDestination = chatDestinationBuilder.build(grpId, RecipientType.GROUP);
            messagingTemplate.convertAndSendToUser(authUser.getUsername(), senderDestination, payload);
        }

        //usr
        var usrMsgList = msgList.stream()
                .filter(msg -> msg.getRecipientType().equals(RecipientType.USER) && !msg.getSender().getId().equals(authUser.getId()))
                .toList();
        if(!usrMsgList.isEmpty()){
            List<Long> sendUsrIds = usrMsgList.stream().map(msg -> msg.getSender().getId()).toList();
            Map<Long, AppUser> senderUsrMap = appUserJpaRepo.findByIdIn(sendUsrIds)
                    .stream()
                    .collect(Collectors.toMap(usr ->usr.getId(), usr -> usr));

            usrMsgList.forEach(msg -> {
                var newStatus = messageStatusTransaction.read(msg.getStatus());
                var sender = senderUsrMap.getOrDefault(msg.getSender().getId(), null);
                if(sender == null) throw new RecordNotFoundException();

                msg.setReadAt(readTime);
                msg.setStatus(newStatus);
                messageJpaRepo.save(msg);
                contactService.markNewUsrMsgAsRead(sender, authUser, 1);
            });

            Set<Long> senderUsrIdSet = new HashSet<>(sendUsrIds);
            senderUsrIdSet.stream().
                    map(usrId -> senderUsrMap.getOrDefault(usrId, null))
                    .filter(Objects::nonNull)
                    .forEach(sender -> {
                        List<MessageDTO> payload = getConversion(authUser, sender.getId(), RecipientType.USER);

                        String destination = chatDestinationBuilder.build(sender.getId(), RecipientType.USER);
                        String senderDestination = chatDestinationBuilder.build(authUser.getId(), RecipientType.USER);

                        messagingTemplate.convertAndSendToUser(authUser.getUsername(), destination, payload);
                        messagingTemplate.convertAndSendToUser(sender.getUsername(), senderDestination, payload);
                    });

        }
    }

    public void notifyUsersOnNewMsg(AppUser sender, long recipientId, RecipientType recipientType){
        if(recipientType == RecipientType.USER){
            List<MessageDTO> payload = getConversion(sender, recipientId, recipientType);

            AppUser recipient = appUserJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new);

            String senderDestination = chatDestinationBuilder.build(recipient.getId(), recipientType);
            String senderUsername = sender.getUsername();
            String recipientDestination = chatDestinationBuilder.build(sender.getId(), recipientType);
            String recipientUsername = recipient.getUsername();
            log.info("Message list size: {}", payload.size());
            messagingTemplate.convertAndSendToUser(senderUsername, senderDestination, payload);
            messagingTemplate.convertAndSendToUser(recipientUsername, recipientDestination, payload);

            contactService.pushNewMsgNotification(sender, recipient);
        }else{
            String groupDestination = chatDestinationBuilder.build(recipientId, recipientType);
            Group grp = groupJpaRepo.findById(recipientId).orElseThrow(RecordNotFoundException::new);
            List<Long> memberUsrIds = memberJpaRepo.findByGroupId(grp.getId()).stream().map(member -> member.getUser().getId()).toList();

            List<AppUser> grpUsers = appUserJpaRepo.findByIdIn(memberUsrIds).stream().toList();
            grpUsers.forEach(user -> {
                List<MessageDTO> msgList = getConversion(user, recipientId, recipientType);
                messagingTemplate.convertAndSendToUser(user.getUsername(), groupDestination, msgList);
                if(!user.getId().equals(sender.getId())){
                    contactService.pushNewGrpMsgNotification(user, grp);
                }
            });
        }
    }
}
