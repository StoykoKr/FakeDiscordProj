import { Component, inject } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserType } from '../../types/user.type';
import { FormsModule } from '@angular/forms';
import {
  DataGridComponent,
  DataGridHeader,
} from '../../components/data-grid/data-grid.component';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'page-user',
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
  imports: [FormsModule, DataGridComponent],
})
export class UserPage {
  public dataGridMapping: DataGridHeader[] = [
    {id:1, column: 'User name', value: 'userName' },
  ];
  public userCollection: UserType[] = [];
  public isCreateFormVisible = false;
  public loggedUser: UserType | null = null;
  private friendCollection: UserType[] = [];
  public showingFriends = false;
  private activeRouter = inject(ActivatedRoute);
  private userService = inject(UserService);
  private router = inject(Router);

  public ngOnInit() {
    var userId = this.activeRouter.snapshot.paramMap.get('id');
    if (userId != null) {
      this.userService.getUserById(+userId).subscribe((data: any) => {
        this.loggedUser = data.data;
        if (this.loggedUser) {
          this.fetchAllFriendsOfUser(this.loggedUser, false); // AAAAAAA Защо съм го направил така?!? ужас.. прави количество излишни неща.. като останалото от програмата, но работи...
          if (
            this.activeRouter.snapshot.url.length == 3 &&
            this.activeRouter.snapshot.url[2].toString() == 'friends'
          ) {
            this.fetchAllFriendsOfUser(this.loggedUser, true);
            this.showingFriends = true;
          } else {
            this.fetchAllActiveUsers();
            this.showingFriends = false;
          }
        }
      });
    } else {
      this.fetchAllActiveUsers();
      this.friendCollection = [];
    }
  }
  public fetchAllActiveUsers() {
    this.userService.getAllActiveUsers().subscribe((data: any) => {
      this.userCollection = data.data;
    });
  }
  public fetchAllFriendsOfUser(user: UserType, changeView: boolean) {
    this.userService.getFriendsOfUser(user).subscribe((data: any) => {
      if (changeView) {
        this.userCollection = data.data;
      }
      this.friendCollection = data.data;
    });
  }
  public processOnDeleteUser(userId: UserType) {
    if (userId.id) {
      this.userService.deleteUser(userId.id).subscribe((result) => {
        console.log(result);
        this.fetchAllActiveUsers();
      });
    }
  }
  public processOnShowChannels() {
    this.router.navigateByUrl(`users/${this.loggedUser?.id}/channels`);
  }

  public processOnLogin(user: UserType) {
    this.router.navigateByUrl(`/users/${user.id}`);
  }
  public processOnLogout() {
    this.router.navigateByUrl(`/users`);
  }
  public processOnCreateUserCreation(inputValue: string) {
    this.userService
      .createNewUser({ userName: inputValue, isActive: 1 })
      .subscribe((res) => {
        this.fetchAllActiveUsers();
        this.isCreateFormVisible = false;
      });
  }

  public processOnAddFriend(friend: UserType) {
    if (this.loggedUser && this.loggedUser.id != friend.id) {
      var isContained = false;
      for (var friendInList of this.friendCollection) {
        if (friendInList.id == friend.id) isContained = true;
      }
      if (!isContained) {
        this.userService
          .addFriendOfUser(this.loggedUser, friend)
          .subscribe((data: any) => {
            if (this.loggedUser)
              this.fetchAllFriendsOfUser(this.loggedUser, false);
          });
      } else {
        // Well.. Ако ми се занимава мога да добавя toast/няква подобна функционалност, която да комуникира грешка..
      }
    } else {
    }
  }
  public processOnRemoveFriend(friendToBeRemoved: UserType) {
    if (this.loggedUser) {
      this.userService
        .removeFriend(this.loggedUser, friendToBeRemoved)
        .subscribe((data: any) => {
          if (this.loggedUser)
            this.fetchAllFriendsOfUser(this.loggedUser, true);
        });
    }
  }

  public processOnOpenChat(user: UserType) {

      this.router.navigateByUrl(
        `users/${this.loggedUser?.id}/friends/${user.id}/chat`)
    
  }
  public processOnShowUsers() {
    //  this.showingFriends = false;
    //   this.fetchAllActiveUsers();
    if (this.loggedUser) {
      this.router.navigateByUrl(`users/${this.loggedUser?.id}`);
    }
  }

  public processOnShowFriends() {
    if (this.loggedUser) {
      this.router.navigateByUrl(`users/${this.loggedUser?.id}/friends`);
    }
    // this.showingFriends = true;
    //  this.fetchAllFriendsOfUser(this.loggedUser,true)
  }
}
