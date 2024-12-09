package com.example.chatapp.db.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column
    Timestamp createAt;

    @Column
    String username;

    @Column
    String password;

    @OneToMany(mappedBy = "user")
    List<Member> groupMembers;

    @OneToMany(mappedBy = "sender")
    List<Message> sentMessages;

    @OneToMany(mappedBy = "requestSender")
    List<Friend> sentFriendRequests;

    @OneToMany(mappedBy = "requestReceiver")
    List<Friend> receivedFriendRequests;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    Set<Role> roles;


    public List<Friend> getFriends(){
        var friends = new ArrayList<Friend>();
        friends.addAll(sentFriendRequests);
        friends.addAll(receivedFriendRequests);
        return friends.stream()
                .sorted((o1, o2) -> Math.toIntExact(o1.getCreateAt().getTime() - o2.getCreateAt().getTime()))
                .toList();
    }
}
