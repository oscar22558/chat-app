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
import {Recipient} from "../model/recipient";
import {Optional} from "../../common/optional";

@Component({
    selector: 'app-conversion',
    templateUrl: './conversion.component.html',
    styleUrls: ['./conversion.component.sass'],
    providers: [SubscribeMessageStompService]
})
export class ConversionComponent implements OnInit, OnDestroy, AfterViewInit {
    private mRecipient?: Recipient | null
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
    ) {
    }

    ngOnInit() {
        if (this.mRecipient == null) return
        this.updateRecipientSubscription(this.mRecipient)
    }

    ngAfterViewInit(): void {
        const ref = this.containerRef?.nativeElement
        console.log(ref)
        ref?.addEventListener("click", () => {
            this.readMsgList()
        })
    }


    ngOnDestroy() {
        this.subscribeMessageService.unsubscribe();
    }

    @Input()
    set recipient(recipient: Optional<Recipient>) {
        this.mRecipient = recipient
        this.updateRecipientSubscription(recipient);
    }

    get recipient(): Optional<Recipient> {
        return this.mRecipient
    }

    updateRecipientSubscription(recipient: Optional<Recipient>) {
        if (recipient == null) {
            this.conversion = []
            this.subscribeMessageService.unsubscribe();
            return
        }
        this.getConversionService
            .get(recipient.recipientId, recipient.recipientType)
            .subscribe(res => this.conversion = res);
        this.subscribeMessageService.subscribe(
            recipient.recipientId,
            recipient.recipientType,
            res => {
                this.conversion = res
            });
    }

    sendMsg() {
        const content = this.msg.value ?? "";
        if(this.recipient == null) return
        this.sendMessageService.send({
            recipientId: this.recipient.recipientId,
            recipientType: this.recipient.recipientType,
            sendAt: new Date(),
            content,
        });
    }

    handleContainFocusIn() {
        this.readMsgList()
    }

    readMsgList(){
        const now = new Date();
        console.log("conversion.component.handleContainFocusIn()")
        const msgIds = this.conversion
            .filter(msg => msg.readAt == null && msg.senderUsername !== this.authService.authedUser?.username && this.authService?.authedUser != null)
            .map(msg => msg.id)
        if(msgIds.length > 0)
            this.readMessageStompService.readMessage({messageIds: msgIds, readTime: now})
    }
}
