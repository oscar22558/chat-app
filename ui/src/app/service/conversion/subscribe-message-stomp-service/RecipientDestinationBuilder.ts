import {RecipientType} from "../../../chat/contacts/model/recipient-type";

export class RecipientDestinationBuilder{
  public build(recipientId: number, recipientType: RecipientType){
    return `/user/queue/msg/${recipientType}/${recipientId}`
  }
}
