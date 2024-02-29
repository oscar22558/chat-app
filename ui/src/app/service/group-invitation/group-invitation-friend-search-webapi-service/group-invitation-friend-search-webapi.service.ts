import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";
import {GroupInvitationFriendSearchResponse} from "./group-invitation-friend-search-response";

@Injectable({
  providedIn: 'root'
})
export class GroupInvitationFriendSearchWebapiService {
  private route = `${WebapiBaseRoute}/group/invitation/friend-search`
  constructor(private http: HttpClient) { }

  get(groupId: number, searchStr: string){
    return this.http.get<GroupInvitationFriendSearchResponse>(this.route, {params: {groupId, search: searchStr}})
  }
}
