import { Injectable } from '@angular/core';
import {AuthWebapiService} from "./auth-webapi-service/auth-webapi.service";
import {AuthRequest} from "./model/auth.request";
import {AppHttpHeaders} from "../app-http-headers";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private token = ""
  constructor(
    private authWebapiService: AuthWebapiService,
    private appHttpHeaders: AppHttpHeaders
  ) { }

  auth(request: AuthRequest, authSuccessHandler?: ()=>void){
    this.authWebapiService.auth(request)
      .subscribe(res => {
        this.token = res.token
        this.appHttpHeaders.setAuthorizationHeader(res.token)
        authSuccessHandler?.()
      })
  }

  isAuthed(): boolean{
    return this.token != null && this.token != ""
  }
}
