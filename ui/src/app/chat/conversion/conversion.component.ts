import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {
  GetConversionWebapiService
} from "../../service/conversion/get-conversion-webapi-service/get-conversion-webapi.service";
import {RecipientType} from "../contacts/model/recipient-type";
import {Conversion} from "../../service/conversion/conversion";
import {FormControl} from "@angular/forms";
import {
  SubscribeMessageStompService
} from "../../service/conversion/subscribe-message-stomp-service/subscribe-message-stomp.service";
import {SendMessageStompService} from "../../service/conversion/send-message-stomp-service/send-message-stomp.service";

@Component({
  selector: 'app-conversion',
  templateUrl: './conversion.component.html',
  styleUrls: ['./conversion.component.sass'],
  providers: [SubscribeMessageStompService]
})
export class ConversionComponent implements OnInit, OnDestroy{
  private _recipientId: number = -1
  private _recipientType: RecipientType = "USER"

  msg = new FormControl("")
  conversion: Conversion = []
  constructor(private service: GetConversionWebapiService,
              private subscribeMessageService: SubscribeMessageStompService,
              private sendMessageService: SendMessageStompService,
              private getConversionService: GetConversionWebapiService,
  ) { }

  ngOnInit() {
    this.updateRecipientSubscription()
  }


  ngOnDestroy() {
    this.subscribeMessageService.unsubscribe();
  }

  get recipientId(): number {
    return this._recipientId;
  }

  @Input()
  set recipientId(value: number) {
    this._recipientId = value;
    this.updateRecipientSubscription();
  }

  get recipientType(): RecipientType {
    return this._recipientType;
  }

  @Input()
  set recipientType(value: RecipientType) {
    this._recipientType = value;
    this.updateRecipientSubscription();
  }

  updateRecipientSubscription(){
    if(this._recipientId == -1){
      this.conversion = []
      this.subscribeMessageService.unsubscribe();
      return
    }
    this.getConversionService
      .get(this._recipientId, this._recipientType)
      .subscribe(res => this.conversion = res);
    this.subscribeMessageService.subscribe(
      this._recipientId,
      this._recipientType,
        res => {
      this.conversion = res
    });
  }

  sendMsg(){
    const content = this.msg.value ?? "";
    this.sendMessageService.send({
      recipientId: this.recipientId ?? -1,
      recipientType: this.recipientType ?? "USER",
      sendAt: new Date(),
      content,
    });
  }
}
