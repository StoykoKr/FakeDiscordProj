package com.project.uniproject.services;

import com.project.uniproject.entities.*;
import com.project.uniproject.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private UserFriendRepository userFriendRepository;
    private ChannelRepository channelRepository;
    private MessageRepository messageRepository;
    private ChannelUserRepository channelUserRepository;

    public ChatService(ChatRepository chatRepository, UserFriendRepository userFriendRepository, ChannelRepository channelRepository, MessageRepository messageRepository,UserRepository userRepository,ChannelUserRepository channelUserRepository) {
        this.chatRepository = chatRepository;
        this.userFriendRepository = userFriendRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelUserRepository = channelUserRepository;
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    //This is probably a bad move but the getChat methods create a chat if one doesn't exist lol
    @Transactional
    public Chat getChatOfUserFriend(int userId, int friendId) {
        Optional<UserFriend> userFriend = userFriendRepository.findByUserIdAndFriendId(userId, friendId);

        if (userFriend.isEmpty())
            userFriend = userFriendRepository.findByUserIdAndFriendId(friendId, userId);

        if (userFriend.isPresent() && userFriend.get().getIsActive()==1) {
            if (userFriend.get().getChat() != null) {
                return userFriend.get().getChat();
            } else {
                Chat chat = new Chat();
                chat.setUserFriend(userFriend.get());
                chat.setIsActive(1);
                chatRepository.save(chat);
                userFriend.get().setChat(chat);

                return chat;
            }
        } else
            return null;
    }
    @Transactional
    public Chat getChatOfChannel(int id,int channelId) {  // Guide how not to make things. Here we can see a single method that does too many things.
        Optional<Chat> chat = chatRepository.findByChannelId(channelId);
        Optional<Channel> channel = channelRepository.findById(channelId);
        Optional<User> user = userRepository.findById(id);
        Optional<ChannelUser> channelUser = channelUserRepository.findByChannelIdAndUserId(channelId,id);
        if(user.isPresent() && user.get().getIsActive()==1 && channelUser.isPresent() && channelUser.get().getIsActive()==1 && channel.isPresent() && channel.get().getIsActive()==1) {
            if (chat.isPresent() && chat.get().getIsActive() == 1) {
                return chat.get();
            }else if (chat.isPresent() && chat.get().getIsActive() == 0) {
                chat.get().setIsActive(1);
                return chat.get();
            }
            else {
                    Chat newChat = new Chat();
                    newChat.setChannel(channel.get());
                    newChat.setIsActive(1);
                    chatRepository.save(newChat);
                    return newChat;

            }
        }
        else{
            return null;
        }
    }

    public boolean deleteChat(int id) {
        Optional<Chat> chat = chatRepository.findByChannelId(id);
        if (chat.isPresent() && chat.get().getIsActive() == 1) {
            chat.get().setIsActive(0);
            return true;
        } else
            return false;
    }

    public Message publishNewMessage(Message message,int senderId,int chatId ) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        Optional<User> sender = userRepository.findById(senderId);
        if(chat.isPresent() && chat.get().getIsActive()==1 && sender.isPresent()&&sender.get().getIsActive()==1){
            message.setChat(chat.get());
            message.setSender(sender.get());
            message.setIsActive(1);
            return messageRepository.save(message);
        }
        return null;
    }

    public Chat getChatById(int id){
        Optional<Chat> chat = chatRepository.findById(id);
        if(chat.isPresent() && chat.get().getIsActive()==1)
            return chat.get();
        return null;
    }


}
