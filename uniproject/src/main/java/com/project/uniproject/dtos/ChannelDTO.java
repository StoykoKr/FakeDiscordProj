package com.project.uniproject.dtos;
import com.project.uniproject.entities.Channel;
import java.util.List;
import java.util.stream.Collectors;


public class ChannelDTO {
    private int id;
    private String channelName;
    private int isActive;

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChannelDTO(Channel channel) {
        this.id = channel.getId();
        this.channelName = channel.getChannelName();
        this.isActive = channel.getIsActive();
    }
}
