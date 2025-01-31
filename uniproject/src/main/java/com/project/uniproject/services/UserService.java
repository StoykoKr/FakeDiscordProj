package com.project.uniproject.services;

import com.project.uniproject.entities.User;
import com.project.uniproject.entities.UserFriend;
import com.project.uniproject.repositories.UserFriendRepository;
import com.project.uniproject.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserFriendRepository userFriendRepository;

    public UserService(UserRepository userRepository, UserFriendRepository userFriendRepository) {
        this.userRepository = userRepository;
        this.userFriendRepository = userFriendRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActive(1);
    }

    public User getUserById(int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        return null;
    }
    @Transactional
    public boolean deleteUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setIsActive(0);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean addNewFriend(int idUser, int idFriend) {
        if(idUser == idFriend)
          return false;
        Optional<User> user = userRepository.findById(idUser);
        Optional<User> friend = userRepository.findById(idFriend);

        if (user.isPresent() && friend.isPresent()) {
            Optional<UserFriend> userFriend = userFriendRepository.findByUserIdAndFriendId(idUser, idFriend);
            Optional<UserFriend> friendUser = userFriendRepository.findByUserIdAndFriendId(idFriend, idUser);
            if ((userFriend.isPresent() && userFriend.get().getIsActive()==0) || (friendUser.isPresent()&&friendUser.get().getIsActive()==0)) {
                if (userFriend.isPresent()) {
                    userFriend.get().setIsActive(1);
                }
                if (friendUser.isPresent())
                {
                    friendUser.get().setIsActive(1);
                }
                return true;
            }
            else if((userFriend.isPresent() && userFriend.get().getIsActive()==1) || (friendUser.isPresent()&&friendUser.get().getIsActive()==1)){
                return false;
            }
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(user.get());
            newUserFriend.setFriend(friend.get());
            newUserFriend.setIsActive(1);
            userFriendRepository.save(newUserFriend);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean removeFriend(int idUser, int idFriend) {
        Optional<User> user = userRepository.findById(idUser);
        Optional<User> friend = userRepository.findById(idFriend);
        if (user.isPresent() && friend.isPresent()) {

            Optional<UserFriend> one = userFriendRepository.findByUserIdAndFriendId(idUser, idFriend);
            Optional<UserFriend> two = userFriendRepository.findByUserIdAndFriendId(idFriend, idUser);

            if (one.isPresent()) {
                one.get().setIsActive(0);

            }

            if (two.isPresent()) {
                two.get().setIsActive(0);

            }
            return true;
        }

        return false;
    }

    public User createNewUser(User user) {
        user.setIsActive(1);
        return userRepository.save(user);
    }

    public List<User> getAllFriendsOf(int id) {

        List<UserFriend> friends = userFriendRepository.findByUserIdOrFriendId(id, id);
        List<User> users = new ArrayList<>();

        for (UserFriend userFriend : friends) {
            if(userFriend.getIsActive() == 1) {
                if (userFriend.getUser().getId() != id && userFriend.getUser().getIsActive() == 1) {
                    users.add(userFriend.getUser());
                } else if (userFriend.getFriend().getId() != id && userFriend.getFriend().getIsActive() == 1) {
                    users.add(userFriend.getFriend());
                }
            }
        }

        return users;
    }
}
