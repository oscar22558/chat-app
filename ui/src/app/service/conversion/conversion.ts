export type Conversion = {
  id: number,
  senderId: number,
  senderUsername: string,
  content: string,
  sentAt: string,
  readAt: string,
  status: 'SENT' | 'READ',
}[]
