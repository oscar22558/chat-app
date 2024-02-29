import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";
import {FriendInvitationSearchUserResponse} from "./friend-invitation-search-user-response";

@Injectable({
  providedIn: 'root'
})
export class FriendInvitationSearchUserWebapiService {

  constructor(private httpClient: HttpClient) { }

  searchUsers(searchStr: string){
    const route = `${WebapiBaseRoute}/friend/invitation/user-search`
    return this.httpClient.get<FriendInvitationSearchUserResponse>(route, {
      params: {
        search: searchStr
      }
    })
  }
}
