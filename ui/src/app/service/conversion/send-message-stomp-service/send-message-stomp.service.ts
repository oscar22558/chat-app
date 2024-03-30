import { Injectable } from '@angular/core';
import {StompConnectionService} from "../../stomp/stomp-connection-service/stomp-connection.service";
import {ChatMessage} from "../chat-message";

@Injectable({
  providedIn: 'root'
})
export class SendMessageStompService {
  private destination = "/app/msg"
  constructor(private stompService: StompConnectionService) { }

  send(msg: ChatMessage){
    this.stompService.getInstance()
      .publish({
        destination: this.destination,
        body: JSON.stringify(msg),
      })
  }
}
