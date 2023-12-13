import {Injectable, OnInit} from '@angular/core';
import {IMessage, RxStomp} from "@stomp/rx-stomp";
import {AuthService} from "../../../service/auth-service/auth.service";
import {ContactResponse} from "../model/contact-response";

@Injectable()
export class ContactsStompService{
  private rxStomp = new RxStomp()
  private socketUrl = "ws://localhost:8080/chat"
  private destination = "/user/queue/contact"
  constructor(private authService: AuthService) { }

  initStomp(): void {
    this.rxStomp.configure({
      connectHeaders: {
        Authorization: this.authService.getAuthorizationToken(),
      },
      brokerURL: this.socketUrl
    })
    this.rxStomp.activate()
  }

  subscribeContactUpdate(subscription?: (contacts: ContactResponse)=>void){
    this.rxStomp
      .watch({ destination: this.destination })
      .subscribe(value => {
        const contacts = JSON.parse(value.body)
        subscription?.(contacts)
      })
  }
}
