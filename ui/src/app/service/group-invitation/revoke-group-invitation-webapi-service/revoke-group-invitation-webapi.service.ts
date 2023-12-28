import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class RevokeGroupInvitationWebapiService {
  constructor(private http: HttpClient) { }

  revoke(groupId: number, userId: number){
    const routeWithRouteVars = `${WebapiBaseRoute}/group/${groupId}/member/${userId}`
    return this.http.delete(routeWithRouteVars)
  }
}
