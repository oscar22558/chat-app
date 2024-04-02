package com.example.chatapp.features.contact;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.common.mapper.DTOConverter;
import com.example.chatapp.converter.UserDTOConverter;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.model.ContactDTO;
import com.example.chatapp.converter.GroupDTOConverter;
import com.example.chatapp.redis.entity.Contact;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactDTOConverter implements DTOConverter<Contact, ContactDTO> {

    GroupJpaRepo groupJpaRepo;
    AppUserJpaRepo appUserJpaRepo;
    GroupDTOConverter groupDTOConverter;

    @Override
    public ContactDTO map(Contact input) {
        var contact = new ContactDTO();
        var userId = input.getUserId();
        var recipientId = input.getRecipientId();
        var recipientType = input.getRecipientType();
        var updatedAt = input.getUpdatedAt();
        var newMsgCount = input.getNewMsgCount();

        contact.setUserId(userId);
        contact.setRecipientId(recipientId);
        contact.setRecipientType(recipientType);
        contact.setUpdatedAt(updatedAt);
        contact.setNewMsgCount(newMsgCount);
        contact.setMsgCount(input.getMsgCount());
        contact.setReadMsgCount(input.getReadMsgCount());

        if(recipientType == RecipientType.GROUP){
            var group = groupJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new);
            var groupDto = groupDTOConverter.map(group);

            var groupInvitationStatus = groupDto.getMemberDTOList()
                    .stream()
                    .filter(memberDTO -> memberDTO.getUserId().equals(userId))
                    .findFirst()
                    .get()
                    .getMemberInvitationStatus();

            groupDto.setInvitationStatus(groupInvitationStatus);
            contact.setRecipientName(groupDto.getName());
            contact.setRecipientGroup(groupDto);
        }else{
            var user = appUserJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new);
            var userDto = new UserDTOConverter().map(user);
            contact.setRecipientName(userDto.getUsername());
            contact.setRecipientUser(userDto);
        }
        return contact;
    }

    @Override
    public Contact reversedMap(ContactDTO output) {
        throw new UnsupportedOperationException();
    }
}
