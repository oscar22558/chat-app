import {Component} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {RecipientType, RecipientTypeList} from "../contacts/model/recipient-type";
import {Conversion} from "../../service/conversion/conversion";

@Component({
  selector: 'app-conversion-debug',
  templateUrl: './conversion-debug.component.html',
  styleUrls: ['./conversion-debug.component.sass']
})
export class ConversionDebugComponent {
  recipientTypeList = RecipientTypeList
    .map(type => ({val: type, title: type.toLowerCase()}))
  conversion: Conversion = []
  recipientFormVals = new FormGroup({
    id: new FormControl(0),
    type: new FormControl<RecipientType>(RecipientType.USER)
  })

  get recipientId(): number{
    return this.recipientFormVals.value.id ?? -1
  }

  get recipientType(): RecipientType{
    return this.recipientFormVals.value.type ?? RecipientType.USER
  }
}
