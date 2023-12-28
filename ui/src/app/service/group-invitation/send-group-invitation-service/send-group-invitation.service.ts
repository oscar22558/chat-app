import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class SendGroupInvitationService {
  constructor(private http: HttpClient) { }

  send(groupId: number, userId: number){
    const route = `${WebapiBaseRoute}/group/${groupId}/member/${userId}`
    return this.http.post<unknown>(route, {})
  }
}
