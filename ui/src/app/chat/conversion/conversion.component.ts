import {AfterViewInit, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
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
import {ReadMessageStompService} from "../../service/conversion/read-message-stomp-service/read-message-stomp.service";
import {AuthService} from "../../service/auth-service/auth.service";

@Component({
  selector: 'app-conversion',
  templateUrl: './conversion.component.html',
  styleUrls: ['./conversion.component.sass'],
  providers: [SubscribeMessageStompService]
})
export class ConversionComponent implements OnInit, OnDestroy, AfterViewInit{
  private _recipientId: number = -1
  private _recipientType: RecipientType = "USER"
  @ViewChild("container")
  containerRef?: ElementRef

  msg = new FormControl("")
  conversion: Conversion = []
  constructor(private service: GetConversionWebapiService,
              private subscribeMessageService: SubscribeMessageStompService,
              private sendMessageService: SendMessageStompService,
              private getConversionService: GetConversionWebapiService,
              private readMessageStompService: ReadMessageStompService,
              private authService: AuthService
  ) { }

  ngOnInit() {
    this.updateRecipientSubscription()

  }

  ngAfterViewInit(): void {
    const ref = this.containerRef?.nativeElement
    console.log(ref)
    ref?.addEventListener("click", () => {
      const now = new Date()
      this.conversion.filter(conversion => conversion.status === "SENT" && conversion.senderUsername !== this.authService.authedUser?.username && this.authService?.authedUser != null)
        .forEach(conversion => {
        if(conversion.status == "SENT"){
          this.readMessageStompService.readMessage({messageId: conversion.id, readTime: now})
        }
      })
    })
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
