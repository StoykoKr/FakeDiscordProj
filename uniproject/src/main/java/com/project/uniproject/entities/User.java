package com.project.uniproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "td_users")
public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    public List<ChannelUser> getChannelUsers() {
        return channelUsers;
    }

    public void setChannelUsers(List<ChannelUser> channelUsers) {
        this.channelUsers = channelUsers;
    }

    public List<UserFriend> getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(List<UserFriend> friendOf) {
        this.friendOf = friendOf;
    }

    public List<UserFriend> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<UserFriend> userFriends) {
        this.userFriends = userFriends;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Message> messages;

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Column(name = "is_active")
    private int isActive;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserFriend> userFriends;

    @OneToMany(mappedBy = "friend")
    @JsonIgnore
    private List<UserFriend> friendOf;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ChannelUser> channelUsers;

    public String getUserName() {
        return userName;
    }
}
