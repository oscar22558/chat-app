import { Injectable } from '@angular/core';
import {RxStomp} from "@stomp/rx-stomp";
import {AuthService} from "../../auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class StompConnectionService {
  private rxStomp?: RxStomp
  private socketUrl = "ws://localhost:8080/chat"
  constructor(private authService: AuthService) { }

  getInstance(): RxStomp {
    if(!this.rxStomp){
      this.rxStomp = new RxStomp()
      this.rxStomp.configure({
        connectHeaders: {
          Authorization: this.authService.getAuthorizationToken(),
        },
        brokerURL: this.socketUrl,
      })
      this.rxStomp.activate()
    }
    return this.rxStomp
  }
}
