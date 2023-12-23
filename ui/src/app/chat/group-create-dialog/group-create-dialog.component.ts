import { Component } from '@angular/core';
import {GroupWebapiService} from "../../service/group/group-webapi-service/group-webapi.service";
import {FormControl, FormGroup} from "@angular/forms";
import {GroupCreateRequest} from "../../service/group/model/group-create-request";
import {MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-group-create-dialog',
  templateUrl: './group-create-dialog.component.html',
  styleUrls: ['./group-create-dialog.component.sass']
})
export class GroupCreateDialogComponent {

  newGroupFrom = new FormGroup({
    name: new FormControl("")
  })
  constructor(
    public dialogRef: MatDialogRef<GroupCreateDialogComponent>,
    private groupWebapiService: GroupWebapiService,
    private snackBar: MatSnackBar
  ) { }

  onCreateGroupClick(){
    const form = this.newGroupFrom.value
    const request: GroupCreateRequest = {
      name: form.name ?? "",
      memberUserIds: []
    }
    this.groupWebapiService.createGroup(request)
      .subscribe(res => {
        this.snackBar.open("Group created!", "OK", {
          duration: 2000
        })
        this.dialogRef.close()
      })
  }
}
