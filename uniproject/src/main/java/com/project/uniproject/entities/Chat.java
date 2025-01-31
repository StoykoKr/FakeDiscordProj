package com.project.uniproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "td_chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private int chatId;

    @OneToOne
    @JoinColumn(name = "channel_id", referencedColumnName = "channel_id", nullable = true)
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }


    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    protected UserFriend getUserFriend() {
        return userFriend;
    }

    public void setUserFriend(UserFriend userFriend) {
        this.userFriend = userFriend;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @OneToOne
    @JoinColumn(name = "user_friend_id", referencedColumnName = "user_friend_id", nullable = true)
    @JsonManagedReference
    private UserFriend userFriend;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @Column(name = "is_active")
    private int isActive;

    public int getChatId() {
        return chatId;
    }
    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

