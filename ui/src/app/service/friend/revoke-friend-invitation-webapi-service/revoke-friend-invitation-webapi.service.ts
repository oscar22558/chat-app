import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class RevokeFriendInvitationWebapiService {

  constructor(private http: HttpClient) { }

  revoke(userId: number){
    const route = `${WebapiBaseRoute}/friend/invitation/${userId}`
    return this.http.delete<unknown>(route)
  }
}
