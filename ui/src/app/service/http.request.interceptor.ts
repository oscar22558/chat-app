import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';
import {AppHttpHeaders} from "./app-http-headers";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  constructor(private appHttpHeaders: AppHttpHeaders) {}

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {

    req = req.clone({
      setHeaders: this.appHttpHeaders.get()
    });

    return next.handle(req);
  }
}
