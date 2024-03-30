package com.example.chatapp.features.friend;

import com.example.chatapp.db.entity.FriendStatus;
import com.example.chatapp.features.friend.model.FriendStatusTransactionException;
import org.springframework.stereotype.Component;

@Component
public class FriendStatusTransaction {
    public FriendStatus sendInvitation(){
        return FriendStatus.PENDING;
    }

    public FriendStatus acceptRequest(FriendStatus oldStatus){
        if(oldStatus != FriendStatus.PENDING)
            throw new FriendStatusTransactionException();
        return FriendStatus.ACCEPTED;
    }

    public FriendStatus block(FriendStatus oldStatus){
       if(oldStatus != FriendStatus.ACCEPTED)
           throw new FriendStatusTransactionException();
       return FriendStatus.BLOCKED;
    }

    public FriendStatus unblock(FriendStatus oldStatus){
        if(oldStatus != FriendStatus.BLOCKED)
            throw new FriendStatusTransactionException();
        return FriendStatus.BLOCKED;
    }

    public FriendStatus revokeOrRejectRequest(FriendStatus oldStatus){
        if(oldStatus != FriendStatus.PENDING)
            throw new FriendStatusTransactionException();
        return FriendStatus.REJECTED;
    }

    public FriendStatus removeFriend(FriendStatus oldStatus){
        if(oldStatus != FriendStatus.ACCEPTED)
            throw new FriendStatusTransactionException();
        return FriendStatus.DELETED;
    }
}
