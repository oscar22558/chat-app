package com.example.chatapp.model;


import com.example.chatapp.db.entity.Member;
import com.example.chatapp.db.entity.MemberInvitationStatus;
import com.example.chatapp.exception.MemberInvitationStatusTransactionException;

public class MemberInvitationStatusTransaction {
    public MemberInvitationStatus sendInvitation(Member member){
        return MemberInvitationStatus.PENDING;
    }

    public Member acceptInvitation(Member member){
        if(member.getInvitationStatus() != MemberInvitationStatus.PENDING)
            throw new MemberInvitationStatusTransactionException();
        member.setInvitationStatus(MemberInvitationStatus.ACCEPTED);
        return member;
    }

    public Member rejectInvitation(Member member){
        if(member.getInvitationStatus() != MemberInvitationStatus.PENDING)
            throw new MemberInvitationStatusTransactionException();
        member.setInvitationStatus(MemberInvitationStatus.REJECTED);
        return member;
    }
}
