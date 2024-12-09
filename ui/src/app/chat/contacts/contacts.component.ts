import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ContactsWebapiService} from "./contacts-webapi-service/contacts-webapi.service";
import {ContactsStompService} from "./contacts-stomp-service/contacts-stomp.service";
import {ContactResponse} from "./model/contact-response";
import {MatDialog} from "@angular/material/dialog";
import {InviteUsersDialogComponent} from "../invite-users-dialog/invite-users-dialog.component";
import {GroupDeleteDialogComponent} from "../group-delete-dialog/group-delete-dialog.component";
import {MemberListDialogComponent} from "../member-list-dialog/member-list-dialog.component";
import {RecipientType, RecipientTypeList} from "./model/recipient-type";
import {Recipient} from "../model/recipient";
import {AuthService} from "../../service/auth-service/auth.service";
import {GroupDto} from "./model/group-dto";
import {GroupWebapiService} from "../../service/group/group-webapi-service/group-webapi.service";
import {GroupRoleType} from "./model/group-role";
import {flatMap, mergeMap} from "rxjs";

@Component({
    selector: 'app-contacts',
    templateUrl: './contacts.component.html',
    styleUrls: ['./contacts.component.sass'],
    providers: [
        ContactsWebapiService,
        ContactsStompService,
    ]
})
export class ContactsComponent implements OnInit, OnDestroy {
    contacts: ContactResponse = []
    @Output()
    onChatStarted = new EventEmitter<Recipient>()

    constructor(
        private contactsWebapiService: ContactsWebapiService,
        private contactsStompService: ContactsStompService,
        private matDialog: MatDialog,
        private authService: AuthService,
        private groupWebapiService: GroupWebapiService,
    ) {
    }

    ngOnInit(): void {
        this.contactsWebapiService
            .getContacts()
            .subscribe(res => {
                console.log(res)
                this.contacts = res
            })
        this.contactsStompService
            .subscribeContactUpdate(newContacts => {
                console.log("update: ", newContacts)
                this.contacts = newContacts
            })
    }

    ngOnDestroy() {
        this.contactsStompService.unsubscribe()
    }

    onInviteUsersClick(groupId: number, groupName: string) {
        const data = {groupId, groupName}
        this.matDialog.open(InviteUsersDialogComponent, {data})
    }

    onGroupDeleteClick(groupId: number, groupName: string) {
        const data = {groupId, groupName}
        this.matDialog.open(GroupDeleteDialogComponent, {data})
    }

    onMemberListClick(groupId: number, groupName: string) {
        const data = {groupId, groupName}
        this.matDialog.open(MemberListDialogComponent, {data})
    }

    onChatClick(recipientId: number, recipientType: RecipientType) {
        this.onChatStarted.emit({recipientId, recipientType})
    }

    onAcceptGroupInvitationClick(groupId: number) {
        if(groupId == null) return
        this.groupWebapiService
            .acceptGroupInvitation(groupId)
            .pipe(
                mergeMap(() => {
                    return this.contactsWebapiService.getContacts()
                })
            )
            .subscribe((contact) => {
                this.contacts = contact
            })
    }

    onRejectGroupInvitationClick(groupId: number) {
        if(groupId == null) return
        this.groupWebapiService
            .rejectGroupInvitation(groupId)
            .pipe(
                mergeMap(() => {
                    return this.contactsWebapiService.getContacts()
                })
            )
            .subscribe((contact) => {
                this.contacts = contact
            })
    }

    isAuthUserInGroupAdminList(groupDto?: GroupDto | null) {
        console.log(groupDto)
        if (groupDto == null) return false;
        const result = groupDto.memberDTOList
            .find((member) => member.userId === this.authService.authedUser?.id && member.groupRole === GroupRoleType.ADMIN) != null
        console.log(this.authService.authedUser?.id, result)
        return result
    }

    protected readonly RecipientType = RecipientType;
}
