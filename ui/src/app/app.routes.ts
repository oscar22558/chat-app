import {Routes} from "@angular/router";
import {SignInOrRegisterComponent} from "./sign-in-or-register/sign-in-or-register.component";
import {ChatComponent} from "./chat/chat.component";

export const appRoutes: Routes = [
  {path: 'sign-in', component: SignInOrRegisterComponent},
  {path: '', component: ChatComponent}
];
