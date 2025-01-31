import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  DataGridHeader,
  DataGridComponent,
} from '../../components/data-grid/data-grid.component';
import { UserService } from '../../services/user.service';
import { UserType } from '../../types/user.type';
import { ChannelType } from '../../types/channel.type';
import { ChannelService } from '../../services/channel.service';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'page-channel',
  templateUrl: './channel.component.html',
  styleUrl: './channel.component.css',
  imports: [DataGridComponent],
})
export class ChannelPage {
  public dataGridMapping: DataGridHeader[] = [];
  public loggedUser: UserType | null = null;
  public dataSourceForInput: [] = [];
  public activeChannel: ChannelType | null = null;
  private activeRouter = inject(ActivatedRoute);
  private router = inject(Router);
  private userService = inject(UserService);
  private channelService = inject(ChannelService);
  private chatService = inject(ChatService);
  public isOnChannelList = false;
  public isOnUserList = false;
  public isOnJoinedUsersList = false;
  public userIsGuest = false;
  public userIsChannelOwner = false;
  public userIsAdmin = false;

  public ngOnInit() {
    var userId = this.activeRouter.snapshot.paramMap.get('id');
    var channelId = this.activeRouter.snapshot.paramMap.get('channelId');

    if (userId != null) {
      if (channelId != null) {
        this.channelService
          .getChannelById(+channelId, +userId)
          .subscribe((data: any) => {
            this.activeChannel = data.data;
          });
      } else {
        this.userService.getUserById(+userId).subscribe((data: any) => {
          this.loggedUser = data.data;
          this.refreshChannelList();
        });
      }
    }
  }
  private refreshChannelList() {
    if (this.loggedUser && this.loggedUser.id) {
      this.channelService
        .getChannelsOfUser(this.loggedUser.id)
        .subscribe((data: any) => {
          this.dataGridMapping = [
            { id: 1, column: 'Channel name', value: 'channelName' },
          ];
          this.dataSourceForInput = data.data;
          this.isOnChannelList = true;
          this.isOnUserList = false;
          this.isOnJoinedUsersList = false;
        });
    }
  }
  private refreshUsersList(channel: ChannelType) {
    if (this.loggedUser && this.loggedUser.id) {
      this.activeChannel = channel;
      this.channelService
        .getUsersOfChannel(channel.id)
        .subscribe((data: any) => {
          this.dataGridMapping = [
            { id: 1, column: 'User name', value: 'user' },
            { id: 2, column: 'Role', value: 'channelRole' },
          ];
          this.dataSourceForInput = data.data;
          this.isOnChannelList = false;
          this.isOnUserList = false;
          this.isOnJoinedUsersList = true;
        });
    }
  }
  private refreshUnjoinedUsersList() {
    if (this.loggedUser && this.loggedUser.id) {
      if(this.activeChannel)
      this.channelService.getUsersNotInChannel(this.activeChannel.id)
        .subscribe((data: any) => {
          this.dataGridMapping = [
            { id: 1, column: 'User name', value: 'userName' }]
          this.dataSourceForInput = data.data;
          this.isOnChannelList = false;
          this.isOnUserList = true;
          this.isOnJoinedUsersList = false;
        });
    }
  }

  private updateCurrentRole(userId: number, channelId: number) {
    this.channelService
      .getUserRoleInChannel(userId, channelId)
      .subscribe((data: any) => {
        if (data.data == 'Owner') {
          this.userIsGuest = false;
          this.userIsChannelOwner = true;
          this.userIsAdmin = false;
        } else if (data.data == 'Admin') {
          this.userIsGuest = false;
          this.userIsChannelOwner = false;
          this.userIsAdmin = true;
        } else if (data.data == 'Guest') {
          this.userIsGuest = true;
          this.userIsChannelOwner = false;
          this.userIsAdmin = false;
        }
      });
  }

  public processOnOpenChannelUserList(channel: ChannelType) {
    this.refreshUsersList(channel);
    if (this.loggedUser?.id)
      this.updateCurrentRole(this.loggedUser?.id, channel.id);
  }
  public processOnDeleteServer() {
    if (this.activeChannel?.isActive == 1 && this.userIsChannelOwner) {
      if (this.loggedUser?.id)
        this.channelService
          .deleteChannel(this.loggedUser.id, this.activeChannel.id)
          .subscribe((data: any) => {
            this.processOnShowChannels();
          });
    }
  }

  public provessOnRemoveUser(user: UserType|any) {
    if (this.loggedUser?.id && this.activeChannel?.id && user.user.id)
      this.channelService
        .removeUserFromChannel(
          this.loggedUser?.id,
          this.activeChannel?.id,
          user.user.id
        )
        .subscribe((data: any) => {
          if (this.activeChannel) this.refreshUsersList(this.activeChannel);
        });
      
  }

  public processOnPromote(user: UserType|any) {
    if (this.loggedUser?.id && this.activeChannel?.id && user.user.id)
      this.channelService
        .promoteUser(this.loggedUser?.id, this.activeChannel?.id, user.user.id)
        .subscribe((data: any) => {
          if (this.activeChannel) this.refreshUsersList(this.activeChannel);
        });
  }
  public processOnRenameChannel(input: string) {
    if (this.loggedUser?.id && this.activeChannel?.id)
      this.channelService.editChannelName(
        this.loggedUser.id,
        input,
        this.activeChannel?.id
      ).subscribe((data:any)=>{
        this.processOnShowChannels();
      })
  }
  public processOnAddUserToChannel(user: UserType) {

    if(this.loggedUser?.id && this.activeChannel?.id && user.id)
    this.channelService.addUserToChannel(this.loggedUser?.id,this.activeChannel?.id,user.id).subscribe((data:any)=>{
      this.refreshUnjoinedUsersList();
    })
   
  }
  public processOnCreateChannel(input: string) {
    if (this.loggedUser?.id)
      this.channelService
        .createNewChannel(this.loggedUser?.id, {
          channelName: input,
          isActive: 1,
        })
        .subscribe((data: any) => {
          this.refreshChannelList();
        });
  }

  public processOnOpenUserListToAdd() {
    this.refreshUnjoinedUsersList()
  }
  public processOnOpenChat(channel: ChannelType) {
    this.router.navigateByUrl(
      `users/${this.loggedUser?.id}/channels/${channel.id}/chat`
    );
  }
  public processOnShowChannels() {
    if (this.loggedUser) this.refreshChannelList();
    this.activeChannel = null;
    this.router.navigateByUrl(`users/${this.loggedUser?.id}/channels`);
  }
  public processOnLogout() {
    this.router.navigateByUrl(`users`);
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
