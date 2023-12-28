import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ContactResponse} from "../model/contact-response";
import {WebapiBaseRoute} from "../../../service/webapi.config";

@Injectable()
//TODO: move under service
export class ContactsWebapiService {
  private route = `${WebapiBaseRoute}/contact`
  constructor(private http: HttpClient) { }

  getContacts(): Observable<ContactResponse>{
    return this.http.get<ContactResponse>(this.route)
  }
}
