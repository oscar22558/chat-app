import {Component, Inject} from '@angular/core';
import {GroupWebapiService} from "../../service/group/group-webapi-service/group-webapi.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {GroupDeleteDialogData} from "./group-delete-dialog-data";

@Component({
  selector: 'app-group-delete-dialog',
  templateUrl: './group-delete-dialog.component.html',
  styleUrls: ['./group-delete-dialog.component.sass']
})
export class GroupDeleteDialogComponent {
  constructor(
    private groupWebapiService: GroupWebapiService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<GroupDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: GroupDeleteDialogData,
  ) { }

  onConfirmClick(){
    const groupId = this.data.groupId
    const groupName = this.data.groupName
    this.groupWebapiService
      .deleteGroup(groupId)
      .subscribe(() => {
        this.snackBar.open(`Group ${groupName} was deleted.`)
        this.dialogRef.close()
      })
  }

  onCancelClick(){
    this.dialogRef.close()
  }

  getGroupName(){
    return this.data.groupName
  }
}
