package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.AppUser;
import com.example.chatapp.db.entity.Message;
import com.example.chatapp.db.entity.RecipientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageJpaRepo extends JpaRepository<Message, Long> {
    List<Message> findMessagesBySenderAndReceiverTypeAndReceiverId(AppUser sender, RecipientType recipientType, long receiverId);
}
