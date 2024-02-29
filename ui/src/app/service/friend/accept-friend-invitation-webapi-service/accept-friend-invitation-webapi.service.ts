import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class AcceptFriendInvitationWebapiService {

  constructor(private http: HttpClient) { }

  accept(userId: number){
    const route = `${WebapiBaseRoute}/friend/invitation/${userId}`
    return this.http.patch(route, {})
  }
}
