import { Component } from '@angular/core';
import {AuthWebapiService} from "../service/auth-service/auth-webapi-service/auth-webapi.service";
import {AuthService} from "../service/auth-service/auth.service";
import {FormControl, FormGroup} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {WebapiBaseRoute} from "../service/webapi.config";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-in-or-register',
  templateUrl: './sign-in-or-register.component.html',
  styleUrls: ['./sign-in-or-register.component.sass'],
  providers: [
    AuthWebapiService
  ]
})
export class SignInOrRegisterComponent {

  authForm = new FormGroup({
    username: new FormControl(""),
    password: new FormControl("")
  })
  constructor(
    private authService: AuthService,
    private httpClient: HttpClient,
    private router: Router
  ) {}

  signInClick(){
    const form = this.authForm.value
    const request = {
      username: form.username ?? "",
      password: form.password ?? ""
    }
    this.authService.auth(request, () => {
      this.router.navigateByUrl("/")
        .then(nav => {})
        .catch(err => console.error(err))
    })
  }

  getUserInfoClick() {
    this.httpClient.get(`${WebapiBaseRoute}/auth/user`).subscribe()
  }
}
