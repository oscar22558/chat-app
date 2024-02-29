import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";
import {FriendListResponse} from "../model/friend-list-response";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FriendListWebapiService {

  constructor(private http: HttpClient) { }

  getFriendList(): Observable<FriendListResponse> {
    const route = `${WebapiBaseRoute}/friend`
    return this.http.get<FriendListResponse>(route)
  }
}
