package com.example.chatapp.redis;

import com.example.chatapp.db.entity.RecipientType;
import com.example.chatapp.redis.entity.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRedisRepo extends CrudRepository<Contact, String> {
    List<Contact> findAllByRecipientType(RecipientType recipientType);
    List<Contact> findAllByUserId(Long userId);
    List<Contact> findAllByUserIdAndRecipientIdAndRecipientType(
            Long userId,
            Long recipientId,
            RecipientType recipientType
    );
    boolean existsAllByUserIdAndRecipientIdAndRecipientType(Long userId, Long recipientId, RecipientType recipientType);
    void deleteAllByUserIdAndRecipientIdAndRecipientType(Long userId, Long recipientId, RecipientType recipientType);
}
