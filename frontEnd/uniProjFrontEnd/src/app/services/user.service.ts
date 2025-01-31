import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { UserType } from '../types/user.type';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private httpClient = inject(HttpClient);
  private baseUrl = `${environment.baseUrl}/users`;
  public getAllUsers() {
    return this.httpClient.get(this.baseUrl);
  }
  public getAllActiveUsers() {
    return this.httpClient.get(`${this.baseUrl}/active`);
  }
  public deleteUser(userId: number) {
    return this.httpClient.delete(`${this.baseUrl}/${userId}`);
  }
  public createNewUser(newUser: UserType) {
    return this.httpClient.post(this.baseUrl, newUser);
  }
  public getUserById(id: number) {
    return this.httpClient.get(`${this.baseUrl}/${id}`);
  }
  public getFriendsOfUser(user: UserType) {
    return this.httpClient.get(`${this.baseUrl}/${user.id}/friends`);
  }
  public addFriendOfUser(user: UserType, friend: UserType) {
    return this.httpClient.post(
      `${this.baseUrl}/${user.id}/addFriend/${friend.id}`,
      null
    );
  }
  public removeFriend(user: UserType, friend: UserType) {
    return this.httpClient.post(
      `${this.baseUrl}/${user.id}/removeFriend/${friend.id}`,
      null
    );
  }
}
