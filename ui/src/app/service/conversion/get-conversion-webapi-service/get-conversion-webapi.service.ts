import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../../webapi.config";
import {RecipientType} from "../../../chat/contacts/model/recipient-type";
import {Conversion} from "../conversion";

@Injectable({
  providedIn: 'root'
})
export class GetConversionWebapiService {

  constructor(private http: HttpClient) { }

  public get(recipientId: number, recipientType: RecipientType){
    const route = `${WebapiBaseRoute}/chat/conversion`
    return this.http.get<Conversion>(route, {
      params: {recipientId, recipientType}
    })
  }
}
