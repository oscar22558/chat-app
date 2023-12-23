import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GroupCreateRequest} from "../model/group-create-request";
import {WebapiBaseRoute} from "../../webapi.config";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GroupWebapiService {
  private route = `${WebapiBaseRoute}/group`
  constructor(private http: HttpClient) { }

  createGroup(groupCreateRequest: GroupCreateRequest): Observable<any> {
    return this.http.post(this.route, groupCreateRequest)
  }

  deleteGroup(groupId: number): Observable<any> {
    const route = `${this.route}/${groupId}`
    return this.http.delete(route)
  }
}
