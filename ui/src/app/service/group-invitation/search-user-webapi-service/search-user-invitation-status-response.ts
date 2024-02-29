import {MemberInvitationStatus} from "../../model/member-invitation-status";

export type SearchUserInvitationStatusResponse = {
  id: number,
  username: string,
  invitationStatus: MemberInvitationStatus,
}[]
