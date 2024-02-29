import {InvitationType} from "../model/invitation-type";

export type FriendInvitationListResponse = {
  type: InvitationType
  userId: number
  username: string
}[]
