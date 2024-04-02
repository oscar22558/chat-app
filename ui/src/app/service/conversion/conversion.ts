export type Conversion = {
  senderId: number,
  senderUsername: string,
  content: string,
  sentAt: string,
  readAt: string,
  status: 'SENT' | 'READ',
}[]
