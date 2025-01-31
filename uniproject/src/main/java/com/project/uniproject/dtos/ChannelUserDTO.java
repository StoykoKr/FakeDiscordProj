package com.project.uniproject.dtos;

import com.project.uniproject.entities.ChannelUser;
import com.project.uniproject.entities.User;

public class ChannelUserDTO {
    private int id;
    private int channelId;
    private String channelRole;
    private int isActive;
    private User user;

    public ChannelUserDTO(ChannelUser channelUser) {
        this.id = channelUser.getId();
        this.channelId = channelUser.getChannel().getId();
        this.channelRole = channelUser.getChannelRole();
        this.isActive = channelUser.getIsActive();
        this.user = channelUser.getChannel_user();

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getChannelRole() {
        return channelRole;
    }

    public void setChannelRole(String channelRole) {
        this.channelRole = channelRole;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
