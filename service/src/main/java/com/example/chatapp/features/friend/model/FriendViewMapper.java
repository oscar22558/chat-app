package com.example.chatapp.features.friend.model;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.Friend;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class FriendViewMapper implements ModelMapper<Friend, FriendView> {
    @Setter
    Long authedUserId = -1L;
    @Override
    public FriendView map(Friend input) {
        var receiverUserId = input.getRequestReceiver().getId();
        var senderUserId = input.getRequestSender().getId();
        var userId = receiverUserId.equals(authedUserId)
                ? senderUserId : receiverUserId;
        var username = receiverUserId.equals(authedUserId)
                ? input.getRequestSender().getUsername()
                : input.getRequestReceiver().getUsername();
        var createAt = input.getCreateAt();
        return new FriendView(userId, createAt, username);
    }

    @Override
    public Friend reversedMap(FriendView output) {
        throw new UnsupportedOperationException();
    }
}