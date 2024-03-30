import {RecipientType} from "../contacts/model/recipient-type";

export type ChatStartedEventModel = {
  recipientId: number
  recipientType: RecipientType
}
