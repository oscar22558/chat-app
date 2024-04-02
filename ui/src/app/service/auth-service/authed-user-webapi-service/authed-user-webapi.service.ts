import { Injectable } from '@angular/core';
import {WebapiBaseRoute} from "../../webapi.config";
import {HttpClient} from "@angular/common/http";
import {AuthedUser} from "../model/authed-user";

@Injectable({
  providedIn: 'root'
})
export class AuthedUserWebapiService {
  private route = `${WebapiBaseRoute}/auth/user`
  constructor(private httpClient: HttpClient) { }

  public getUser(){
    return this.httpClient.get<AuthedUser>(this.route)
  }
}
