package com.example.chatapp.features.friend.model;

import com.example.chatapp.common.mapper.ModelMapper;
import com.example.chatapp.db.entity.Friend;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InvitationViewMapper implements ModelMapper<Friend, InvitationView> {
    @Setter
    Long authedUserId;

    @Override
    public InvitationView map(Friend input) {
        InvitationType type = Objects.equals(authedUserId, input.getRequestSender().getId())
                ? InvitationType.SENT : InvitationType.RECEIVED;
        String username = type == InvitationType.SENT
                ? input.getRequestReceiver().getUsername() : input.getRequestSender().getUsername();
        Long userId = type == InvitationType.SENT
                ? input.getRequestReceiver().getId() : input.getRequestSender().getId();
        return new InvitationView(type, userId, username);
    }

    @Override
    public Friend reversedMap(InvitationView output) {
        throw new UnsupportedOperationException();
    }
}
