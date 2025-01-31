package com.project.uniproject.repositories;

import com.project.uniproject.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel,Integer> {
   // Optional<Channel> findById(int Id);
}
