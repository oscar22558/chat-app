package com.example.chatapp.features.contact;

import com.example.chatapp.common.AppTimestamp;
import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Contact;
import com.example.chatapp.db.entity.Group;
import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.db.repo.ContactJpaRepo;
import com.example.chatapp.db.repo.GroupJpaRepo;
import com.example.chatapp.features.contact.model.ContactView;
import com.example.chatapp.features.user.UserIdentityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ContactService {
    UserIdentityService userIdentityService;
    ContactViewMapper mapper;
    AppUserJpaRepo appUserJpaRepo;
    ContactJpaRepo contactJpaRepo;
    ContactUpdatePushService contactUpdatePushService;

    public List<ContactView> getContact(){
        var authedUser = userIdentityService.getUser();
        var contacts = authedUser.getContacts();
        return mapper.map(contacts);
    }

    public void addContact(AppUser user, Group group){
        var contacts = user.getContacts();
        var newContact = new Contact();
        newContact.setRecipientId(group.getId());
        newContact.setRecipientType(RecipientType.GROUP);
        newContact.setUpdatedAt(AppTimestamp.newInstance());
        newContact.setUser(user);
        var savedContact = contactJpaRepo.save(newContact);

        contacts.add(savedContact);
        appUserJpaRepo.save(user);
        contactUpdatePushService.push(user);
    }

    public void addContact(AppUser user, AppUser recipient){
        var userContact = new Contact();
        userContact.setUpdatedAt(AppTimestamp.newInstance());
        userContact.setRecipientId(recipient.getId());
        userContact.setRecipientType(RecipientType.USER);
        userContact.setUser(user);
        user.getContacts().add(userContact);
        contactJpaRepo.save(userContact);

        var recipientContact = new Contact();
        recipientContact.setUpdatedAt(AppTimestamp.newInstance());
        recipientContact.setRecipientId(user.getId());
        recipientContact.setRecipientType(RecipientType.USER);
        recipientContact.setUser(recipient);
        recipient.getContacts().add(recipientContact);
        contactJpaRepo.save(recipientContact);

        contactUpdatePushService.push(user);
        contactUpdatePushService.push(recipient);
    }

    public void removeContact(AppUser user, Group group){
        var groupContact = user.getContacts().stream()
                .filter(contact -> contact.getRecipientId().equals(group.getId())
                        && contact.getRecipientType() == RecipientType.GROUP
                        )
                .findFirst();
        groupContact.ifPresent(contact -> {
            user.getContacts().remove(contact);
            contactJpaRepo.delete(contact);
        });
    }
}
