import {Routes} from "@angular/router";
import {SignInOrRegisterComponent} from "./sign-in-or-register/sign-in-or-register.component";
import {ChatComponent} from "./chat/chat.component";
import {FriendComponent} from "./friend/friend.component";
import {ConversionComponent} from "./chat/conversion/conversion.component";
import {ConversionDebugComponent} from "./chat/conversion-debug/conversion-debug.component";

export const appRoutes: Routes = [
  {path: 'sign-in', component: SignInOrRegisterComponent},
  {path: '', component: ChatComponent},
  {path: 'friend', component: FriendComponent},
  {path: 'chat', component: ConversionComponent},
  {path: 'chat-debug', component: ConversionDebugComponent},
];
