import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {appRoutes} from "./app.routes";
import {RouterModule} from "@angular/router";
import { ChatComponent } from './chat/chat.component';
import {SignInOrRegisterComponent} from "./sign-in-or-register/sign-in-or-register.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpRequestInterceptor} from "./service/http.request.interceptor";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ContactsComponent } from './chat/contacts/contacts.component';
import {AuthService} from "./service/auth-service/auth.service";
import {AppHttpHeaders} from "./service/app-http-headers";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { GroupCreateDialogComponent } from './chat/group-create-dialog/group-create-dialog.component';
import {MatInputModule} from "@angular/material/input";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MatSnackBarModule} from "@angular/material/snack-bar";

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    SignInOrRegisterComponent,
    ContactsComponent,
    GroupCreateDialogComponent,
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
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
