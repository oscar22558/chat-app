export type GroupInvitationFriendSearchResponse = {
  id: number,
  username: string,
  status: "PENDING" | "ACCEPTED" | "",
}[]
