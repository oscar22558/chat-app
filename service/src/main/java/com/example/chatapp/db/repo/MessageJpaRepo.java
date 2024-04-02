package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Message;
import com.example.chatapp.db.entity.MessageStatus;
import com.example.chatapp.db.entity.RecipientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MessageJpaRepo extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipientTypeAndRecipientId(AppUser sender, RecipientType recipientType, Long recipientId);
    List<Message> findByRecipientIdAndRecipientType(Long recipientId, RecipientType recipientType);
    List<Message> findByRecipientIdAndRecipientTypeAndStatus(Long recipientId, RecipientType recipientType, MessageStatus status);

    List<Message> findByIdIn(Collection<Long> ids);
}
