import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { UserType } from '../types/user.type';
import { environment } from '../environments/environment';
import { MessageType } from '../types/message.type';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private httpClient = inject(HttpClient);
  private baseUrl = `${environment.baseUrl}`;

  public getChatOfUserFriend(userid: number, friendId: number) {
    return this.httpClient.get(
      `${this.baseUrl}/users/${userid}/friends/${friendId}/chat`
    );
  }
  public getUserChatOfChannel(userid: number, channelId: number) {
    return this.httpClient.get(
      `${this.baseUrl}/users/${userid}/channels/${channelId}/chat`
    );
  }
  public postMessageToChat(userid: number, chatId: number, message: any) {
    return this.httpClient.post(
      `${this.baseUrl}/users/${userid}/chat/${chatId}`,
      message
    );
  }
  public getChatById(chatid: number) {
    return this.httpClient.get(`${this.baseUrl}/chats/${chatid}`);
  }
}
