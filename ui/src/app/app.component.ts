import {Component, OnInit} from '@angular/core';
import {AuthService} from "./service/auth-service/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit{
  title = 'chat-app';
  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    if(!this.authService.isAuthed()){
      this.redirectUserToSingInPage()
    }
  }

  onLogOutClick(){
    this.authService.removeAuth()
    this.redirectUserToSingInPage()
  }

  private redirectUserToSingInPage(){
    this.router.navigateByUrl("/sign-in")
      .then(nav => {})
      .catch(err => console.log(err))
  }

  isLogOutBtnShown() {
    return this.authService.isAuthed()
  }
}
