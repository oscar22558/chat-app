import { Injectable } from '@angular/core';
import {StompConnectionService} from "../../stomp/stomp-connection-service/stomp-connection.service";
import {ReadMessageRequest} from "../read-message-request";

@Injectable({
  providedIn: 'root'
})
export class ReadMessageStompService {
  private destination = "/app/msg/read"
  constructor(private stompService: StompConnectionService) { }

  readMessage(request: ReadMessageRequest) {
    this.stompService.getInstance()
      .publish({
        destination: this.destination,
        body: JSON.stringify(request)
      })
  }
}
