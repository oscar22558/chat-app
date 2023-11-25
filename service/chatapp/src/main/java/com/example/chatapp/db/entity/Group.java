package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column(nullable = false)
    String name;

    @OneToMany(mappedBy = "group")
    List<Member> members;

    public void addMember(Member member){
        members.add(member);
    }

    public void removeMember(Member member){
       members.remove(member);
    }
}
