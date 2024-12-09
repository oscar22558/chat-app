import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {ContactResponse} from "../model/contact-response";
import {WebapiBaseRoute} from "../../../service/webapi.config";
import {GroupRoleType} from "../model/group-role";

@Injectable()
//TODO: move under service
export class ContactsWebapiService {
  private route = `${WebapiBaseRoute}/contact`
  constructor(private http: HttpClient) { }

  getContacts(): Observable<ContactResponse>{
    return this.http.get<ContactResponse>(this.route)
        .pipe(
            map(res => {
                return res.map(contact => {
                    if(contact.recipientGroup == null) return contact

                    const adminMemberDTOList = contact
                        .recipientGroup
                        .memberDTOList
                        .filter((member) => member.groupRole === GroupRoleType.ADMIN)
                    return {
                       ...contact,
                        recipientGroup: {
                           ...contact.recipientGroup,
                           // adminMemberDTOList
                       }
                   }
                })
            })
        )
  }
}
