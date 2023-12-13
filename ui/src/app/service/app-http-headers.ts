import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppHttpHeaders{
  private headers: { [p: string]: string } = {}
  constructor() {
    this.set("Content-Type", "application/json")
  }

  setAuthorizationHeader(token: string) {
    this.set("Authorization", `Bearer ${token}`)
  }

  set(key: string, value: string){
    this.headers[key] = value
  }

  get(){
    return this.headers
  }
}
