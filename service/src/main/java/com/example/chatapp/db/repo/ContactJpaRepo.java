package com.example.chatapp.db.repo;

import com.example.chatapp.db.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactJpaRepo extends JpaRepository<Contact, Long> {
}
