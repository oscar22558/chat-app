import {RecipientType} from "../../chat/contacts/model/recipient-type";

export type ChatMessage = {
  recipientId: number,
  recipientType: RecipientType,
  sendAt: Date,
  content: string
}
