package com.project.uniproject.repositories;

import com.project.uniproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    List<User> findByIsActive(int isActive);

    @Query("SELECT u FROM User u WHERE u.isActive = 1 AND (u NOT IN (SELECT cu.user FROM ChannelUser cu WHERE cu.channel.id = :channel_id) OR u IN (SELECT cu.user FROM ChannelUser cu WHERE cu.channel.id = :channel_id AND cu.isActive = 0))")
    List<User> getAllUsersThatAreNotInTheSpecifiedChannel(int channel_id);
}
