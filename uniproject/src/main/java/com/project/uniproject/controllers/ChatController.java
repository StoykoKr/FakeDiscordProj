package com.project.uniproject.controllers;

import com.project.uniproject.entities.Message;
import com.project.uniproject.entities.User;
import com.project.uniproject.http.Responses;
import com.project.uniproject.services.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {
    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chats")
    public ResponseEntity<?> getAllChats() {

        var collection = chatService.getAllChats();
        return Responses.success()
                .withMessage("All chats")
                .withData(collection)
                .build();
    }

    @GetMapping("/users/{id}/friends/{friendId}/chat")
    public ResponseEntity<?> getChatOfUserFriend(@PathVariable int id, @PathVariable int friendId) {
        var chat = chatService.getChatOfUserFriend(id, friendId);

        if(chat != null)
            return Responses.success()
                .withMessage("Friends chat")
                .withData(chat)
                .build();

        return Responses.error()
                .withMessage("Error opening friends chat. Not friends or user/s don't exist")
                .withData(chat)
                .build();
    }

    @GetMapping("/users/{id}/channels/{channelId}/chat")
    public ResponseEntity<?> getChatOfChannel(@PathVariable int id, @PathVariable int channelId) {
        var chat = chatService.getChatOfChannel(id,channelId);

        if (chat == null)
            return Responses.error()
                    .withMessage("Chat error. Incorrect ids or missing permissions")
                    .withCode(HttpStatus.BAD_REQUEST)
                    .withData(chat)
                    .build();

        return Responses.success()
                .withMessage("Channel chat")
                .withData(chat)
                .build();
    }


    @DeleteMapping("/chats/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable int id) {
        var chat = chatService.deleteChat(id);


        if (!chat)
            return Responses.error()
                    .withMessage("Chat not found")
                    .withCode(HttpStatus.BAD_REQUEST)
                    .withData(chat)
                    .build();

        return Responses.success()
                .withMessage("Chat deleted")
                .withData(chat)
                .build();
    }

    @PostMapping("/users/{id}/chat/{chatId}")
    public ResponseEntity<?> publishMessage(@RequestBody Message message,@PathVariable int id,@PathVariable int chatId) {
        var response = chatService.publishNewMessage(message,id,chatId);

        if(response != null)
        return Responses.success()
                .withMessage("Message created")
                .withData(response)
                .build();

        return Responses.error()
                .withMessage("Missing or wrong chat or sender")
                .withCode(HttpStatus.BAD_REQUEST)
                .withData(response)
                .build();
    }
    @GetMapping("/chats/{id}")
    public ResponseEntity<?> getChatById(@PathVariable int id){
        var response = chatService.getChatById(id);

        if(response != null)
            return Responses.success()
                    .withMessage("Provided chat by id")
                    .withData(response)
                    .build();

        return Responses.error()
                .withMessage("Incorrect id")
                .withCode(HttpStatus.BAD_REQUEST)
                .withData(response)
                .build();
    }

}
