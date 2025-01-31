package com.project.uniproject.repositories;

import com.project.uniproject.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Integer> {

    Optional<Chat> findByChannelId(int channelId);
}
