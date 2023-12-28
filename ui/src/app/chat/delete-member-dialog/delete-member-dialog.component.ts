import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DeleteMemberDialogData} from "./delete-member-dialog-data";
import {
  GroupMemberWebapiService
} from "../../service/group-member/group-member-webapi-service/group-member-webapi.service";

@Component({
  selector: 'app-delete-member-dialog',
  templateUrl: './delete-member-dialog.component.html',
  styleUrls: ['./delete-member-dialog.component.sass']
})
export class DeleteMemberDialogComponent {
  constructor(
    private dialogRef: MatDialogRef<DeleteMemberDialogComponent>,
    private groupMemberWebapiService: GroupMemberWebapiService,
    @Inject(MAT_DIALOG_DATA) public data: DeleteMemberDialogData,
  ) { }

  onConfirm(){
    const {userId, groupId} = this.data
    this.groupMemberWebapiService
      .deleteMember(groupId, userId)
      .subscribe(() => this.dialogRef.close())
  }
  onCancelClick(){
    this.dialogRef.close()
  }

  getUsername(){
    return this.data.username
  }
}
