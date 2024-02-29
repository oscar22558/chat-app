import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";
import {InvitationType} from "../model/invitation-type";
import {FriendInvitationListResponse} from "./friend-invitation-list-response";

@Injectable({
  providedIn: 'root'
})
export class FriendInvitationListWebapiService {

  constructor(private http: HttpClient) { }

  getInvitations(invitationType: InvitationType) {
    const route = `${WebapiBaseRoute}/friend/invitation`
    return this.http.get<FriendInvitationListResponse>(route, {params: {type: invitationType}})
  }
}
