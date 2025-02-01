package com.project.uniproject.controllers;

import com.project.uniproject.dtos.ChannelUserDTO;
import com.project.uniproject.entities.Channel;
import com.project.uniproject.entities.ChannelUser;
import com.project.uniproject.http.Responses;
import com.project.uniproject.services.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChannelController {
    private ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/users/{userId}/channels/createChannel")
    public ResponseEntity<?> createChannel(@RequestBody Channel channel, @PathVariable int userId) {
        var response = channelService.createChannel(channel, userId);
        if (response != null) {
            return Responses.success().withMessage("Channel created").withData(response).build();
        } else {
            return Responses.error().withMessage("Error creating channel").withData(response).build();
        }
    }

    @GetMapping("/channels")
    public ResponseEntity<?> showAllChannels() {
        var response = channelService.getAllChannels();
        return Responses.success().withMessage("All channels").withData(response).build();

    }

    @DeleteMapping("/users/{userId}/channels/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable int userId,@PathVariable int channelId) {
        var response = channelService.deleteChannel(channelId, userId);
        if (response==0) {
            return Responses.success().withMessage("Channel deleted").build();
        } else if(response==1){
            return Responses.error().withMessage("Error deleting channel. User not owner!").withCode(HttpStatus.FORBIDDEN).build();
        }
        else{
            return Responses.error().withMessage("Error deleting channel. Could not find user or channel.. or a connection between them").withCode(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/users/{invitingUserId}/channels/{channelId}/addUser/{invitedId}")
    public ResponseEntity<?> addUserToChannel(@PathVariable int invitingUserId,@PathVariable int channelId,@PathVariable int invitedId) {
        var response = channelService.addUserToChannel(invitingUserId, channelId, invitedId);

        if (response != null) {
            return Responses.success().withMessage("User added to channel as guest").withData(response).build();
        }
        return Responses.error().withMessage("Error adding user to channel").withData(response).build();
    }

    @PostMapping("/users/{userId}/channels")
    public ResponseEntity<?> updateChannelName(@PathVariable int userId, @RequestBody Channel updatedChannel) {
        var response = channelService.updateChannel(userId, updatedChannel);

        if (response.getIsActive() == 1) {
            return Responses.success().withMessage("Channel updated").withData(response).build();
        } else if (response.getIsActive() == -1) {
            return Responses.error().withMessage("Error with updating channel. User lacks permission").withCode(HttpStatus.FORBIDDEN).build();
        }
        return Responses.error().withMessage("Error with updating channel. User or channel or their connection don't exist").withCode(HttpStatus.NOT_FOUND).build();

    }

    @PostMapping("/users/{promotingUserId}/channels/{channelId}/promote/{promotedUserId}")
    public ResponseEntity<?> promoteUserInChannel(@PathVariable int promotingUserId,@PathVariable int channelId,@PathVariable int promotedUserId) {
        var response = channelService.promoteUser(promotingUserId, promotedUserId, channelId);

        if (response != null) {
            return Responses.success().withMessage("Promotion complete").withData(response).build();
        }
        return Responses.error().withMessage("Error promoting users").withData(response).build();

    }
    @DeleteMapping("/users/{ownerId}/channels/{channelId}/delete/{removedUserID}")
    public ResponseEntity<?> removeUserFromChannel(@PathVariable int ownerId,@PathVariable int channelId,@PathVariable int removedUserID) {
        var isRemoved = channelService.removeUserFromChannel(ownerId,channelId,removedUserID);
        if(isRemoved){
          return Responses.success().withMessage("User removed from channel").withData(isRemoved).build();
        }
        return Responses.error().withMessage("Could not perform the removal.").withData(isRemoved).build();
    }

    @GetMapping("/channels/{channelId}/users")
    public ResponseEntity<?> showAllUsersOfChannel(@PathVariable int channelId) {
        var response = channelService.getAllUsersOfChannel(channelId);

        if (response != null) {
            return Responses.success().withMessage("Showing all active users of channel").withData(response).build();
        }
        return Responses.error().withMessage("Channel does not exist").withData(response).build();
    }
    @GetMapping("/users/{userId}/channels/{channelId}")
    public ResponseEntity<?> getChannelById(@PathVariable int userId,@PathVariable int channelId) {
        var response = channelService.getChannelByID(channelId,userId);
        if (response != null) {
            return Responses.success().withMessage("Showing channel joined by user").withData(response).build();
        }
        return Responses.error().withMessage("Channel does not exist or user is not joined").withData(response).build();
    }
    @GetMapping("/users/{userId}/channels/{channelId}/userRole")
    public ResponseEntity<?> getUserRoleInChannel(@PathVariable int userId,@PathVariable int channelId) {
        var response = channelService.getUserRoleInChannel(channelId,userId);
        if (response != null) {
            return Responses.success().withMessage("Showing the role of a user in channel").withData(response).build();
        }
        return Responses.error().withMessage("Not existing channel or user or connection between them.").withData(response).build();
    }


    @GetMapping("/channels/{channelId}/foreignUsers")
    public ResponseEntity<?> showAllUsersThatAreNotInChannel(@PathVariable int channelId) {
        var response = channelService.getAllUsersNotInChannel(channelId);
        if (response != null) {
            return Responses.success().withMessage("Showing all users not in the specified channel").withData(response).build();
        }
        return Responses.error().withMessage("No users? or no idea how this threw error").withData(response).build();
    }

    @GetMapping("/users/{userId}/channels")
    public ResponseEntity<?> getAllChannelsOfUser(@PathVariable int userId) {
        var list = channelService.getAllChannelsOfUser(userId);
        return Responses.success().withMessage("Showing all active channels of user").withData(list).build();

    }


}
