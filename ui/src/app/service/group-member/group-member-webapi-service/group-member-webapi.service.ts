import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";
import {GroupMemberListResponse} from "./group-member-list-response";

@Injectable({
  providedIn: 'root'
})
export class GroupMemberWebapiService {

  constructor(private http: HttpClient) { }

  getMemberList(groupId: number){
    const route = `${WebapiBaseRoute}/group/${groupId}/member`
    return this.http.get<GroupMemberListResponse>(route, {
      params: {status: "ACCEPTED"}
    })
  }

  deleteMember(groupId: number, userId: number){
    const route = `${WebapiBaseRoute}/group/${groupId}/member/${userId}`
    return this.http.delete<unknown>(route)
  }
}
