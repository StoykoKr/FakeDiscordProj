import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ChannelService {
  private httpClient = inject(HttpClient);
  private baseUrl = `${environment.baseUrl}`;

  public getChannelsOfUser(userId: number) {
    return this.httpClient.get(`${this.baseUrl}/users/${userId}/channels`);
  }
  public createNewChannel(userid: number, channel: any) {
    return this.httpClient.post(
      `${this.baseUrl}/users/${userid}/channels/createChannel`,
      channel
    );
  }
  public getChannelById(channelId: number, userId: number) {
    return this.httpClient.get(
      `${this.baseUrl}/users/${userId}/channels/${channelId}`
    );
  }
  public getUsersOfChannel(channelId: number) {
    return this.httpClient.get(`${this.baseUrl}/channels/${channelId}/users`);
  }
  public promoteUser(promotingUserId:number,channelId:number,promotedUserId:number){
    return this.httpClient.post(`${this.baseUrl}/users/${promotingUserId}/channels/${channelId}/promote/${promotedUserId}`,null);
  }
  public removeUserFromChannel(ownerUserId:number,channelId:number,removedUserId:number){
    return this.httpClient.delete(`${this.baseUrl}/users/${ownerUserId}/channels/${channelId}/delete/${removedUserId}`);
  }
  public getUserRoleInChannel(userId:number,channelId:number){
    return this.httpClient.get(`${this.baseUrl}/users/${userId}/channels/${channelId}/userRole`);
  }
  public editChannelName(userId:number,newName:string, channelId:number){
    return this.httpClient.post(`${this.baseUrl}/users/${userId}/channels`,{id:channelId,channelName:newName});
  }
  public deleteChannel(userId:number, channelId:number){
    return this.httpClient.delete(`${this.baseUrl}/users/${userId}/channels/${channelId}`);
  }
  public getUsersNotInChannel(channelId:number){
    return this.httpClient.get(`${this.baseUrl}/channels/${channelId}/foreignUsers`);
  }
  public addUserToChannel(invitingUserId:number,channelId:number,invitedId:number){
    return this.httpClient.post(`${this.baseUrl}/users/${invitingUserId}/channels/${channelId}/addUser/${invitedId}`,null);
  }

}
