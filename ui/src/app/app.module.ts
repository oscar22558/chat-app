import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {appRoutes} from "./app.routes";
import {RouterModule} from "@angular/router";
import { ChatComponent } from './chat/chat.component';
import {SignInOrRegisterComponent} from "./sign-in-or-register/sign-in-or-register.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpRequestInterceptor} from "./service/http.request.interceptor";
import {ReactiveFormsModule} from "@angular/forms";
import { ContactsComponent } from './chat/contacts/contacts.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { GroupCreateDialogComponent } from './chat/group-create-dialog/group-create-dialog.component';
import {MatInputModule} from "@angular/material/input";
import {MatDialogModule} from "@angular/material/dialog";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { GroupDeleteDialogComponent } from './chat/group-delete-dialog/group-delete-dialog.component';
import { InviteUsersDialogComponent } from './chat/invite-users-dialog/invite-users-dialog.component';
import {MatTabsModule} from "@angular/material/tabs";
import { InviteFriendsTabpageComponent } from './chat/invite-users-dialog/invite-friends-tabpage/invite-friends-tabpage.component';
import { InviteOtherUsersTabpageComponent } from './chat/invite-users-dialog/invite-other-users-tabpage/invite-other-users-tabpage.component';
import { MemberListDialogComponent } from './chat/member-list-dialog/member-list-dialog.component';
import { DeleteMemberDialogComponent } from './chat/delete-member-dialog/delete-member-dialog.component';
import { LeaveGroupConfirmDialogComponent } from './chat/leave-group-confirm-dialog/leave-group-confirm-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    SignInOrRegisterComponent,
    ContactsComponent,
    GroupCreateDialogComponent,
    GroupDeleteDialogComponent,
    GroupDeleteDialogComponent,
    InviteUsersDialogComponent,
    InviteFriendsTabpageComponent,
    InviteOtherUsersTabpageComponent,
    MemberListDialogComponent,
    DeleteMemberDialogComponent,
    LeaveGroupConfirmDialogComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(
      appRoutes, {enableTracing: true}  // <-- debugging purposes only
    ),
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatDialogModule,
    MatSnackBarModule,
    MatTabsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
