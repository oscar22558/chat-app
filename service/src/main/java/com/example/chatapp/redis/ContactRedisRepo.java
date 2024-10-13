package com.example.chatapp.redis;

import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.redis.entity.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRedisRepo extends CrudRepository<Contact, String> {
    List<Contact> findAllByUserId(Long userId);
    List<Contact> findAllByUserIdAndRecipientIdAndAndRecipientType(Long userId, Long recipientId, RecipientType recipientType);
    boolean existsAllByUserIdAndRecipientIdAndAndRecipientType(Long userId, Long recipientId, RecipientType recipientType);
    void deleteAllByUserIdAndRecipientIdAndAndRecipientType(Long userId, Long recipientId, RecipientType recipientType);
}
