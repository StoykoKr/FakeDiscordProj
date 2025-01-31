import {
  Component,
  EventEmitter,
  input,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { UserType } from '../../types/user.type';

export type DataGridHeader = {
  id: number;
  column: string;
  value: string;
};

@Component({
  selector: 'custom-data-grid',
  templateUrl: './data-grid.component.html',
  styleUrl: './data-grid.component.css',
})
export class DataGridComponent {
  public inputPage = 0;
  @Input() public inputHeaderConfig: DataGridHeader[] = [];
  @Input() public inputDataSource: any;
  @Input() public inputLoggedUser: UserType | null = null;

  @Input() public inputIsOnChatPage = false;

  @Input() public inputIsCurrentlyShowingFriends = false;
  @Input() public inputIsRemovable = false;
  @Input() public inputIsAMenu = false;
  @Input() public inputIsBefriendable = false;

  @Input() public inputHasChat = false;

  @Input() public inputIsOnChannelList = false;
  @Input() public inputIsOnUserList = false;
  @Input() public inputIsOnJoinedUsersList = false;

  @Input() public inputUserIsGuest = false;
  @Input() public inputUserIsChannelOwner = false;
  @Input() public inputUserIsAdmin = false;

  ngOnChanges(changes: SimpleChanges) {
    // BEHOLD! The worst possible page mechanism
    if (this.inputIsOnChatPage) {
      if (changes['inputDataSource']) {
        let count = Array.isArray(this.inputDataSource)
          ? this.inputDataSource.length
          : 0;
        this.inputPage = Math.floor(count / 10);
      }
    }
  }

  public onClickGoingLeft() {
    this.inputPage -= 1;
    if (this.inputPage < 0) this.inputPage = 0;
  }
  public onClickGoingRight() {
    this.inputPage += 1;
    if (this.inputPage < 0) this.inputPage = 0;
  }

  @Output()
  public onOpenChat = new EventEmitter();

  @Output()
  public onAddNewUsersToChannel = new EventEmitter();

  @Output()
  public onAddUserToCurrentChannel = new EventEmitter();

  @Output()
  public onAddFriend = new EventEmitter();

  @Output()
  public onCreate = new EventEmitter();

  @Output()
  public onLogout = new EventEmitter();

  @Output()
  public onUserList = new EventEmitter();

  @Output()
  public onFriendList = new EventEmitter();

  @Output()
  public onChannel = new EventEmitter();

  @Output()
  public onPromote = new EventEmitter();

  @Output()
  public onEdit = new EventEmitter();

  @Output()
  public onRemove = new EventEmitter();

  @Output()
  public onRemoveFriend = new EventEmitter();

  @Output()
  public onLogin = new EventEmitter();
}
