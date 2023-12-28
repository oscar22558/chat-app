import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FriendListWebapiService {
  private route = "/friend"
  constructor(private http: HttpClient) { }

  get(){
    return this.http.get(this.route)

  }
}
