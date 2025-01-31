import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  DataGridComponent,
  DataGridHeader,
} from '../../components/data-grid/data-grid.component';
import { MessageType } from '../../types/message.type';
import { ActivatedRoute, Router } from '@angular/router';
import { UserType } from '../../types/user.type';
import { ChatService } from '../../services/chat.service';
import { UserService } from '../../services/user.service';
import { ChatType } from '../../types/chat.type';

@Component({
  selector: 'page-chat',
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css',
  imports: [FormsModule, DataGridComponent],
})
export class ChatPage {
  public dataGridMapping: DataGridHeader[] = [
    {id:1, column: 'Send by', value: 'sender' },
    {id:2, column: 'Message content', value: 'messageContent' },
  ];
  public loggedUser: UserType | null = null;
  public currentChat: ChatType | null = null;
  private activeRouter = inject(ActivatedRoute);
  private router = inject(Router);
  private chatService = inject(ChatService);
  private userService = inject(UserService);
  public ngOnInit() {
    var userId = this.activeRouter.snapshot.paramMap.get('id');
    var userChatId = this.activeRouter.snapshot.paramMap.get('userChatId');
    var channelChatId =
      this.activeRouter.snapshot.paramMap.get('channelChatId');
    if (userId != null) {
      this.userService.getUserById(+userId).subscribe((data: any) => {
        this.loggedUser = data.data;
      });
    }

    if (userChatId != null && userId != null) {
      this.chatService
        .getChatOfUserFriend(+userId, +userChatId)
        .subscribe((data: any) => {
          this.currentChat = {
            chatId: data.data.chatId,
            messages: data.data.messages,
            isActive: data.data.isActive,
          };
        });
    }

    if (channelChatId != null && userId != null) {
      this.chatService
        .getUserChatOfChannel(+userId, +channelChatId)
        .subscribe((data: any) => {
          this.currentChat = {
            chatId: data.data.chatId,
            messages: data.data.messages,
            isActive: data.data.isActive,
          };
        });
    }
  }
  private updateChat() {
    if (this.currentChat)
      this.chatService
        .getChatById(this.currentChat.chatId)
        .subscribe((data: any) => {
          this.currentChat = data.data;
        });
  }

  public processOnMessagePublication(inputText: string) {
    if (this.loggedUser?.id && this.currentChat?.chatId)
      this.chatService
        .postMessageToChat(this.loggedUser?.id, this.currentChat?.chatId, {
          messageContent: inputText,
        })
        .subscribe((data: any) => {
          this.updateChat();
        });
  }
  public processOnLogout() {
    this.router.navigateByUrl(`users`);
  }
  public processOnShowChannels() {
    this.router.navigateByUrl(`users/${this.loggedUser?.id}/channels`);
  }
  public processOnShowUsers() {
    if (this.loggedUser) {
      this.router.navigateByUrl(`users/${this.loggedUser?.id}`);
    }
  }

  public processOnShowFriends() {
    if (this.loggedUser) {
      this.router.navigateByUrl(`users/${this.loggedUser?.id}/friends`);
    }
  }
}
