import { Injectable } from '@angular/core';
import {AuthWebapiService} from "./auth-webapi-service/auth-webapi.service";
import {AuthRequest} from "./model/auth.request";
import {AppHttpHeaders} from "../app-http-headers";
import {LocalStorageService} from "../local-storage-service/local-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private token = ""
  private storageKey = "access_token"
  constructor(
    private authWebapiService: AuthWebapiService,
    private appHttpHeaders: AppHttpHeaders,
    private localStorageService: LocalStorageService,
  ) {
    const accessToken = localStorageService.getData(this.storageKey)
    if(accessToken){
      this.token = accessToken
      this.appHttpHeaders.setAuthorizationHeader(accessToken)
    }
  }

  auth(request: AuthRequest, authSuccessHandler?: ()=>void){
    this.authWebapiService.auth(request)
      .subscribe(res => {
        this.token = res.token
        this.appHttpHeaders.setAuthorizationHeader(res.token)
        this.localStorageService.saveData(this.storageKey, this.token)
        authSuccessHandler?.()
      })
  }

  isAuthed(): boolean{
    return this.token != null && this.token != ""
  }

  getAuthorizationToken(){
    return `Bearer ${this.token}`
  }

  removeAuth(){
    this.token = ""
    this.appHttpHeaders.removeAuthorizationHeader()
    this.localStorageService.removeData(this.storageKey)
  }
}
