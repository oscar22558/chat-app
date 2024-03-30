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
  private _recipientId?: number
  private _recipientType?: RecipientType

  msg = new FormControl("")
  conversion: Conversion = []
  constructor(private service: GetConversionWebapiService,
              private subscribeMessageService: SubscribeMessageStompService,
              private sendMessageService: SendMessageStompService,
              private getConversionService: GetConversionWebapiService,
  ) { }

  ngOnInit() {
    if(this._recipientId != null && this._recipientType){
      this.service
        .get(this._recipientId, this._recipientType)
        .subscribe(res => this.conversion = res)
      this.subscribeMessageService.subscribe(res => {
        this.conversion = res
      });
    }
  }


  ngOnDestroy() {
    this.subscribeMessageService.unsubscribe();
  }

  get recipientId(): number | undefined {
    return this._recipientId;
  }

  @Input()
  set recipientId(value: number) {
    this._recipientId = value;
    this.updateRecipientSubscription();
  }

  get recipientType(): RecipientType | undefined {
    return this._recipientType;
  }

  @Input()
  set recipientType(value: RecipientType) {
    this._recipientType = value;
    this.updateRecipientSubscription();
  }

  updateRecipientSubscription(){
    if(this._recipientId == null || this._recipientType == null) return;
    this.getConversionService
      .get(this._recipientId, this._recipientType)
      .subscribe(res => this.conversion = res);
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
