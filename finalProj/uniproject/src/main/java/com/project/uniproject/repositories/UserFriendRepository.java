package com.project.uniproject.repositories;

import com.project.uniproject.entities.User;
import com.project.uniproject.entities.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriend,Integer> {

    Optional<UserFriend> findByUserIdAndFriendId(int userId, int friendId);
    List<UserFriend> findByUserIdOrFriendId(int userId, int friendId);

}
