package com.project.uniproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;


@Entity
@Table(name = "td_channels_users")
@JsonView()
public class ChannelUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_user_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "channel_role", nullable = false)
    private String channelRole;

    @Column(name = "is_active")
    private int isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getChannel_user() {
        return user;
    }

    public void setChannel_user(User channel_user) {
        this.user = channel_user;
    }

    public String getChannelRole() {
        return channelRole;
    }

    public void setChannelRole(String channelRole) {
        this.channelRole = channelRole;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
