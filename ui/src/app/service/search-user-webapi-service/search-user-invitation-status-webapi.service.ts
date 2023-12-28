import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SearchUserInvitationStatusResponse} from "./search-user-invitation-status-response";
import {WebapiBaseRoute} from "../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class SearchUserInvitationStatusWebapiService {
  private route = `${WebapiBaseRoute}/user/invitation-status/search`
  constructor(private http: HttpClient) { }

  search(groupId: number, username: string){
    return this.http.get<SearchUserInvitationStatusResponse>(this.route, {
      params: {groupId, username}
    })
  }
}
