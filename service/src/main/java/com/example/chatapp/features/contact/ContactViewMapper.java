package com.example.chatapp.features.contact;

import com.example.chatapp.common.exception.RecordNotFoundException;
import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.Contact;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.features.contact.model.ContactView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactViewMapper implements ModelMapper<Contact, ContactView> {

    GroupJpaRepo groupJpaRepo;
    AppUserJpaRepo appUserJpaRepo;

    @Override
    public ContactView map(Contact input) {
        var userId = input.getUser().getId();
        var recipientId = input.getRecipientId();
        var recipientType = input.getRecipientType();

        var recipientName = "";
        if(recipientType == RecipientType.GROUP){
            recipientName = groupJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getName();
        }else{
            recipientName = appUserJpaRepo.findById(recipientId)
                    .orElseThrow(RecordNotFoundException::new)
                    .getUsername();
        }

        var updatedAt = input.getUpdatedAt();
        return new ContactView(userId, recipientId, recipientType, recipientName, updatedAt);
    }

    @Override
    public Contact reversedMap(ContactView output) {
        throw new UnsupportedOperationException();
    }
}
