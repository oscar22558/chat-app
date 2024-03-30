import { Injectable } from '@angular/core';
import {StompConnectionService} from "../../stomp/stomp-connection-service/stomp-connection.service";
import {Conversion} from "../conversion";
import {Subscription} from "rxjs";

@Injectable()
export class SubscribeMessageStompService {
  private destination = "/user/queue/msg"
  private sub?: Subscription
  constructor(private stompService: StompConnectionService) { }


  subscribe(resHandler?: (conversion: Conversion) => void){
    if(this.sub) this.unsubscribe()
    this.sub = this.stompService.getInstance()
      .watch({destination: this.destination})
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
