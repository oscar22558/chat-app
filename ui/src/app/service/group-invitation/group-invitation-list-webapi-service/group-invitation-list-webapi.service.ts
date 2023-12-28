import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GroupInvitationListResponse} from "./group-invitation-list-response";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class GroupInvitationListWebapiService {
  constructor(private http: HttpClient) { }

  get(groupId: number){
    const route = `${WebapiBaseRoute}/group/${groupId}/member`
    return this.http.get<GroupInvitationListResponse>(route, {
      params: {status: "PENDING"}
    })
  }
}
