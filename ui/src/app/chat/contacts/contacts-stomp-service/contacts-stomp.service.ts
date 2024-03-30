import {Injectable} from '@angular/core';
import {RxStomp} from "@stomp/rx-stomp";
import {AuthService} from "../../../service/auth-service/auth.service";
import {ContactResponse} from "../model/contact-response";
import {Subscription} from "rxjs";
import {StompConnectionService} from "../../../service/stomp/stomp-connection-service/stomp-connection.service";

@Injectable()
//TODO: move under service
export class ContactsStompService{
  private destination = "/user/queue/contact"
  private sub?: Subscription
  constructor(private stompService: StompConnectionService) { }

  subscribeContactUpdate(subscription?: (contacts: ContactResponse)=>void){
    if(this.sub) this.unsubscribe()
    this.sub = this.stompService
      .getInstance()
      .watch({ destination: this.destination })
      .subscribe(value => {
        const contacts = JSON.parse(value.body)
        subscription?.(contacts)
      })
    console.info("Contact stomp subscribed.")
  }

  unsubscribe(){
    if(!this.sub) return
    this.sub?.unsubscribe()
    console.info("Contact stomp un-subscribed.")
    this.sub = undefined
  }
}
