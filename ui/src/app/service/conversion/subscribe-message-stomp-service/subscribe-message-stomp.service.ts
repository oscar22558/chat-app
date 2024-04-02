import { Injectable } from '@angular/core';
import {StompConnectionService} from "../../stomp/stomp-connection-service/stomp-connection.service";
import {Conversion} from "../conversion";
import {Subscription} from "rxjs";
import {RecipientType} from "../../../chat/contacts/model/recipient-type";
import {RecipientDestinationBuilder} from "./RecipientDestinationBuilder";

@Injectable()
export class SubscribeMessageStompService {
  private destinationBuilder = new RecipientDestinationBuilder()
  private sub?: Subscription
  constructor(private stompService: StompConnectionService) { }


  subscribe(recipientId: number, recipientType: RecipientType, resHandler?: (conversion: Conversion) => void){
    if(this.sub) this.unsubscribe()
    this.sub = this.stompService.getInstance()
      .watch({destination: this.destinationBuilder.build(recipientId, recipientType)})
      .subscribe(res => {
        const conversion = JSON.parse(res.body)
        resHandler?.(conversion)
      })
    console.info("Message stomp subscribed.")
  }

  unsubscribe(){
    if(!this.sub) return
    this.sub?.unsubscribe()
    console.info("Message stomp un-subscribed.")
    this.sub = undefined
  }
}
