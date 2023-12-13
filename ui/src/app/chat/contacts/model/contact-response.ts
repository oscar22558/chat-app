import {RecipientType} from "./recipient-type";

export type ContactResponse = {
  userId: number;
  recipientId: number;
  recipientType: RecipientType;
  recipientName: string;
  updatedAt: string;
}[]
