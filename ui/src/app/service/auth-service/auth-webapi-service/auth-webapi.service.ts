import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthRequest} from "../model/auth.request";
import {WebapiBaseRoute} from "../../webapi.config";
import {AuthResponse} from "../model/auth.response";
import {Observable} from "rxjs";
import {AppHttpHeaders} from "../../app-http-headers";

@Injectable({
  providedIn: "root"
})
export class AuthWebapiService {
  private route = `${WebapiBaseRoute}/auth`
  constructor(private httpClient: HttpClient) { }

  auth(request: AuthRequest): Observable<AuthResponse> {
    return this.httpClient.post<AuthResponse>(
      this.route,
      request,
    )
  }
}
