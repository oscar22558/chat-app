import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";

@Injectable({
  providedIn: 'root'
})
export class LeaveGroupWebapiService {

  constructor(private http: HttpClient) { }

  leaveGroup(groupId: number){
    const route = `${WebapiBaseRoute}/group/${groupId}/member`
    return this.http.delete(route)
  }
}
