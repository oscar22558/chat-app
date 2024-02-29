import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class RejectFriendInvitationWebapiService {

  constructor(private http: HttpClient) { }

  reject(userId: number){
    const route = `${WebapiBaseRoute}/friend/invitation/${userId}`
    return this.http.delete(route)
  }
}
