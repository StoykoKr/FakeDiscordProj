package com.project.uniproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "td_channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<ChannelUser> getChannelUsers() {
        return channelUsers;
    }

    public void setChannelUsers(List<ChannelUser> channelUsers) {
        this.channelUsers = channelUsers;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Column(name = "channel_name", nullable = false)
    private String channelName;
    @Column(name = "is_active")
    private int isActive;
    @OneToMany(mappedBy = "channel")
    @JsonIgnore
    private List<ChannelUser> channelUsers;
}
