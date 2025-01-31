ALTER TABLE td_chats
ADD CONSTRAINT fk_user_friend
FOREIGN KEY (user_friend_id)
REFERENCES td_user_friends(user_friend_id);