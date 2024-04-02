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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public void addMember(Member member){
        members.add(member);
    }

    public void addMembers(List<Member> members){
        this.members.addAll(members);
    }

    public void removeMember(Member member){
       members.remove(member);
    }

    public void removeMembers(List<Member> members){
        this.members.removeAll(members);
    }
}
