import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class SendFriendInvitationWebapiService {

  constructor(private http: HttpClient) { }

  sendRequest(userId: number){
    const route = `${WebapiBaseRoute}/friend`
    return this.http.post<unknown>(route, {userId})
  }
}
