package com.example.chatapp.features.friend;

import com.example.chatapp.db.entity.FriendStatus;
import org.springframework.stereotype.Component;

@Component
public class FriendStatusChecker {
    public boolean isValid(FriendStatus oldStatus, FriendStatus newStatus){
        if(oldStatus == FriendStatus.PENDING){
            return newStatus == FriendStatus.ACCEPTED;
        }else if(oldStatus == FriendStatus.ACCEPTED){
            return newStatus == FriendStatus.BLOCKED;
        }else{
            return newStatus == FriendStatus.ACCEPTED;
        }
    }
}
