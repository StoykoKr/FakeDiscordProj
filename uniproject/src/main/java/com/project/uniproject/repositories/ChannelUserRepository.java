package com.project.uniproject.repositories;

import com.project.uniproject.entities.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelUserRepository extends JpaRepository<ChannelUser,Integer> {
    Optional<ChannelUser> findByChannelIdAndUserId(int channelId,int userId);
    List<ChannelUser> findByUserId(int userId);
}
