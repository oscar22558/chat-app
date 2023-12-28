import {Component, Inject} from '@angular/core';
import {
  LeaveGroupWebapiService
} from "../../service/group-member/leave-group-webapi-service/leave-group-webapi.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {LeaveGroupConfirmDialogData} from "./leave-group-confirm-dialog-data";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LeaveGroupConfirmDialogResult} from "./leave-group-confirm-dialog-result";

@Component({
  selector: 'app-leave-group-confirm-dialog',
  templateUrl: './leave-group-confirm-dialog.component.html',
  styleUrls: ['./leave-group-confirm-dialog.component.sass']
})
export class LeaveGroupConfirmDialogComponent {

  constructor(
    private leaveGroupWebapiService: LeaveGroupWebapiService,
    private matDialogRef: MatDialogRef<LeaveGroupConfirmDialogComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: LeaveGroupConfirmDialogData,
  ){}

  onConfirm(){
    const groupId = this.data.groupId
    const groupName = this.data.groupName
    this.leaveGroupWebapiService
      .leaveGroup(groupId)
      .subscribe(() => {
        this.snackBar.open(`You have left ${groupName}.`, "OK", {duration: 1000})
        this.matDialogRef.close(LeaveGroupConfirmDialogResult)
      })
  }

  onCancel(){
    this.matDialogRef.close()
  }

  getGroupName() {
    return this.data.groupName
  }
}
