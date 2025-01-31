import { UserType } from "./user.type"

export type MessageType = {
    id:number
    messageContent: string
    sender: UserType
}