import {RecipientType} from "./recipient-type";
import {UserDto} from "./user-dto";
import {GroupDto} from "./group-dto";

export type ContactResponse = ContactDto[]

export type ContactDto = {
    userId: number;
    recipientId: number;
    recipientType: RecipientType;
    recipientName: string;
    updatedAt: string;
    newMsgCount: number;
    msgCount: number;
    readMsgCount: number;
    recipientUser?: UserDto|null;
    recipientGroup?: GroupDto|null;
}
