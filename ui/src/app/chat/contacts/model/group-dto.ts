import {MemberDto} from "./member-dto";
import {MemberInvitationStatus} from "../../../service/model/member-invitation-status";

export type GroupDto = {
    id: number;
    username: number;
    memberDTOList: MemberDto[]
    adminMemberDTOList: MemberDto[]
    invitationStatus: MemberInvitationStatus
}
