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

  removeAuthorizationHeader(){
    this.remove("Authorization")
  }

  set(key: string, value: string){
    this.headers[key] = value
  }

  remove(key: string){
    delete this.headers[key]
  }

  get(){
    return this.headers
  }
}
