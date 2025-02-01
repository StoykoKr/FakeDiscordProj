package com.project.uniproject.services;

import com.project.uniproject.dtos.ChannelDTO;
import com.project.uniproject.dtos.ChannelUserDTO;
import com.project.uniproject.entities.Channel;
import com.project.uniproject.entities.ChannelUser;
import com.project.uniproject.entities.User;
import com.project.uniproject.repositories.ChannelRepository;
import com.project.uniproject.repositories.ChannelUserRepository;
import com.project.uniproject.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {
    private ChannelRepository channelRepository;
    private ChannelUserRepository channelUserRepository;
    private UserRepository userRepository;

    public ChannelService(ChannelRepository channelRepository, ChannelUserRepository channelUserRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.channelUserRepository = channelUserRepository;
        this.userRepository = userRepository;
    }

    public List<ChannelDTO> getAllChannels() {
        var chanList = channelRepository.findAll();
        var returningList = new ArrayList<ChannelDTO>();
        for (Channel channel : chanList) {
            if(channel.getIsActive()==1){
                returningList.add(new ChannelDTO(channel));
            }
        }

        return returningList;
    }
    public ChannelDTO getChannelByID(int channelId, int userId){
        var chan = channelRepository.findById(channelId);
        var user = userRepository.findById(userId);
        var userChan = channelUserRepository.findByChannelIdAndUserId(channelId,userId);
        if(chan.isPresent() && chan.get().getIsActive()==1){
            if(user.isPresent() && user.get().getIsActive()==1){
                if(userChan.isPresent() && userChan.get().getIsActive()==1){
                    return new ChannelDTO(chan.get());
                }
            }
        }
        return null;
    }

    @Transactional
    public boolean removeUserFromChannel(int ownerId, int channelId, int removedUserID){
        var channel = channelRepository.findById(channelId);
        var owner = userRepository.findById(ownerId);
        var removed = userRepository.findById(removedUserID);
        if(channel.isPresent() && channel.get().getIsActive()==1 && owner.isPresent() && owner.get().getIsActive()==1 && removed.isPresent() && removed.get().getIsActive()==1){
            var ownerRole = channelUserRepository.findByChannelIdAndUserId(channelId,ownerId);
            if(ownerRole.isPresent() && ownerRole.get().getIsActive()==1 && ownerRole.get().getChannelRole().equals("Owner")){
                var toBeRemoved = channelUserRepository.findByChannelIdAndUserId(channelId,removedUserID);
                if(toBeRemoved.isPresent() && toBeRemoved.get().getIsActive()==1){
                    toBeRemoved.get().setIsActive(0);
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    public String getUserRoleInChannel(int channelId, int userId){
        var chan = channelRepository.findById(channelId);
        var user = userRepository.findById(userId);
        var userChan = channelUserRepository.findByChannelIdAndUserId(channelId,userId);
        if(chan.isPresent() && chan.get().getIsActive()==1){
            if(user.isPresent() && user.get().getIsActive()==1){
                if(userChan.isPresent() && userChan.get().getIsActive()==1){
                    return userChan.get().getChannelRole();
                }
            }
        }
        return null;
    }
    public ChannelDTO createChannel(Channel channel, int creatorId) {
        Optional<User> user = userRepository.findById(creatorId);
        if (user.isPresent()) {
            channel.setIsActive(1);
            Channel ch = channelRepository.save(channel);
            ChannelUser channelUser = new ChannelUser();
            channelUser.setChannel(ch);
            channelUser.setChannel_user(user.get());
            channelUser.setChannelRole("Owner");
            channelUser.setIsActive(1);
            var channelUserRelation = channelUserRepository.save(channelUser);
            List<ChannelUser> temp = new ArrayList<>();
            temp.add(channelUserRelation);
            ch.setChannelUsers(temp);
            return new ChannelDTO(ch);
        }
        return null;
    }

    @Transactional
    public int deleteChannel(int channelId, int userId) {
        Optional<ChannelUser> channelUser = channelUserRepository.findByChannelIdAndUserId(channelId, userId);
        if (channelUser.isPresent()) {
            if(channelUser.get().getChannelRole().equals("Owner")){
            if (channelUser.get().getChannel().getIsActive() == 1) {
                channelUser.get().getChannel().setIsActive(0);
                return 0;
                 }
            }
            return 1;
        }
        return 2;
    }

    public List<User> getAllUsersNotInChannel(int channelId) {
        return userRepository.getAllUsersThatAreNotInTheSpecifiedChannel(channelId);
    }

    public ChannelUserDTO addUserToChannel(int invitingUserId, int channelId, int invitedId) {
        Optional<Channel> channel = channelRepository.findById(channelId);
        if (channel.isEmpty() || channel.get().getIsActive() == 0) {
            return null;
        }
        Optional<User> invitingUser = userRepository.findById(invitingUserId);
        Optional<ChannelUser> invitingChannelUser = channelUserRepository.findByChannelIdAndUserId(channelId, invitingUserId);
        if (invitingUser.isEmpty() ||
                invitingUser.get().getIsActive() == 0 ||
                invitingChannelUser.isEmpty() ||
                invitingChannelUser.get().getChannelRole().equals("Guest") ||
                invitingChannelUser.get().getIsActive() == 0
        ) {
            return null;
        }

        Optional<User> invitedUser = userRepository.findById(invitedId);
        if (invitedUser.isEmpty() || invitedUser.get().getIsActive() == 0) {
            return null;
        }

        Optional<ChannelUser> newchannelUser = channelUserRepository.findByChannelIdAndUserId(channelId, invitedId);
        if (newchannelUser.isPresent()) {
            if(newchannelUser.get().getIsActive() == 1){
                return null;
            }
            else if (newchannelUser.get().getIsActive() == 0){
                ChannelUser newlyAddedChannelUser = newchannelUser.get();
                newlyAddedChannelUser.setIsActive(1);
                newlyAddedChannelUser.setChannelRole("Guest");
                var newuser = channelUserRepository.save(newlyAddedChannelUser);
                return new ChannelUserDTO(newuser);
            }
        }

        ChannelUser newlyAddedChannelUser = new ChannelUser();
        newlyAddedChannelUser.setIsActive(1);
        newlyAddedChannelUser.setChannel(channel.get());
        newlyAddedChannelUser.setChannel_user(invitedUser.get());
        newlyAddedChannelUser.setChannelRole("Guest");
        var newUser = channelUserRepository.save(newlyAddedChannelUser);
        return new ChannelUserDTO(newUser);
    }

    public List<ChannelUserDTO> getAllUsersOfChannel(int channelId) {
        Optional<Channel> channel = channelRepository.findById(channelId);
        if (channel.isPresent() && channel.get().getIsActive() == 1) {

            List<ChannelUser> listOfAll = channel.get().getChannelUsers();
            List<ChannelUserDTO> listOfAllActive = new ArrayList<>();
            for (ChannelUser chUser : listOfAll) {
                if (chUser.getIsActive() == 1) {
                    listOfAllActive.add(new ChannelUserDTO(chUser));
                }
            }
            return listOfAllActive;
        }
        return null;
    }


    public ChannelDTO updateChannel(int userId, Channel updatedChannel) {

        Optional<ChannelUser> channelUser = channelUserRepository.findByChannelIdAndUserId(updatedChannel.getId(), userId);

        if (channelUser.isPresent() && channelUser.get().getIsActive() == 1) {
            if((channelUser.get().getChannelRole().equals("Admin") || channelUser.get().getChannelRole().equals("Owner"))) {
                Optional<Channel> channel = channelRepository.findById(updatedChannel.getId());
                if (channel.isPresent() && channel.get().getIsActive() == 1) {
                    channel.get().setChannelName(updatedChannel.getChannelName());
                    channelRepository.save(channel.get());
                    return new ChannelDTO(channel.get());
                }
                return  new ChannelDTO(2);
            }
            return new ChannelDTO(1);
        }
        return new ChannelDTO(2);
    }

    @Transactional
    public ChannelUserDTO promoteUser(int promotingId, int promotedId, int channelId) {
        Optional<Channel> channel = channelRepository.findById(channelId);

        if (channel.isEmpty() || channel.get().getIsActive() == 0) {
            return null;
        }
        Optional<ChannelUser> promotedUser = channelUserRepository.findByChannelIdAndUserId(channelId, promotedId);

        Optional<ChannelUser> promotingUser = channelUserRepository.findByChannelIdAndUserId(channelId, promotingId);


        if (promotingUser.isEmpty() || promotedUser.isEmpty() || promotingUser.get().getIsActive() == 0 || promotedUser.get().getIsActive() == 0) {
            return null;
        }
        if (promotingUser.get().getChannelRole().equals("Owner")) {
            promotedUser.get().setChannelRole("Admin");

            return new ChannelUserDTO(promotedUser.get());
        }
        return null;
    }

    public List<ChannelDTO> getAllChannelsOfUser(int userId){
        var list = channelUserRepository.findByUserId(userId);
        List<ChannelDTO> response = new ArrayList<>();
        for (ChannelUser channelUser : list) {
            if(channelUser.getIsActive()==1 && channelUser.getChannel().getIsActive()==1) {
                response.add(new ChannelDTO(channelUser.getChannel()));
            }
        }
        return response;
    }

}
