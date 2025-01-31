package com.project.uniproject.controllers;

import com.project.uniproject.entities.User;
import com.project.uniproject.http.Responses;
import com.project.uniproject.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {

        var collection = userService.getAllUsers();
        //      var collection = userService.getAllActiveUsers();

        return Responses.success()
                .withMessage("All users")
                .withData(collection)
                .build();
    }

    @GetMapping("/users/active")
    public ResponseEntity<?> getAllActiveUsers() {

        var collection = userService.getAllActiveUsers();
        //var collection = userService.getAllUsers();
        return Responses.success()
                .withMessage("All active users")
                .withData(collection)
                .build();
    }

    @PostMapping("/users")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        //user.setIsActive(1);
        var response = userService.createNewUser(user);
        return Responses.success().withMessage("User created").withData(response).build();
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        var response = userService.getUserById(id);
        if(response != null)
            return Responses.success().withMessage("User created").withData(response).build();
        return Responses.error().withMessage("Error finding user with this id").withData(response).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        var response = userService.deleteUser(id);
        if (response) {
            return Responses.success().withMessage("User deleted").withData(response).build();
        } else {
            return Responses.error().withMessage("Error with deletion").withData(response).withCode(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/users/{user_id}/addFriend/{friend_id}")
    public ResponseEntity<?> addFriend(@PathVariable int user_id, @PathVariable int friend_id) {
        var response = userService.addNewFriend(user_id, friend_id);
        if (response) {
            return Responses.success().withMessage("Friend added").withData(response).build();
        } else {
            return Responses.error().withMessage("Error with adding friend").withData(response).withCode(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/users/{user_id}/removeFriend/{friend_id}")
    public ResponseEntity<?> removeFriend(@PathVariable int user_id, @PathVariable int friend_id) {
        var response = userService.removeFriend(user_id, friend_id);
        if (response) {
            return Responses.success().withMessage("Friend removed").withData(response).build();
        } else {
            return Responses.error().withMessage("Error with removing friend").withData(response).withCode(HttpStatus.BAD_REQUEST).build();
        }

    }


    @GetMapping("/users/{id}/friends")
    public ResponseEntity<?> getAllFriends(@PathVariable int id) {
        var response = userService.getAllFriendsOf(id);
        return Responses.success().withMessage("Showing friends").withData(response).build();

    }


}
