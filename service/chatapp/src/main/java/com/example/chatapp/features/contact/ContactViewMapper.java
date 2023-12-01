package com.example.chatapp.features.contact;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.Contact;
import com.example.chatapp.features.contact.model.ContactView;
import org.springframework.stereotype.Component;

@Component
public class ContactViewMapper implements ModelMapper<Contact, ContactView> {

    @Override
    public ContactView map(Contact input) {
        var userId = input.getUser().getId();
        var recipientId = input.getRecipientId();
        var recipientType = input.getRecipientType();
        var updatedAt = input.getUpdatedAt();
        return new ContactView(userId, recipientId, recipientType, updatedAt);
    }

    @Override
    public Contact reversedMap(ContactView output) {
        throw new UnsupportedOperationException();
    }
}
