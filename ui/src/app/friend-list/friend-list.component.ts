import {Component, OnInit} from '@angular/core';
import {FriendListWebapiService} from "../service/friend/friend-list-webapi-service/friend-list-webapi.service";
import {FriendListResponse} from "../service/friend/model/friend-list-response";
import {catchError} from "rxjs";
import GenericHttpErrorHandler from "../service/generic-http-error-handler";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrls: ['./friend-list.component.sass']
})
export class FriendListComponent implements OnInit{
  friendList: FriendListResponse = []

  constructor(private friendListWebapiService: FriendListWebapiService) { }

  ngOnInit() {
    this.fetchFriendList()
  }

  fetchFriendList(){
    this.friendListWebapiService
      .getFriendList()
      .pipe(
        catchError(new GenericHttpErrorHandler().build())
      )
      .subscribe(res => this.friendList = res)
  }
}
