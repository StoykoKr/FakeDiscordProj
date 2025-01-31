CREATE TABLE IF NOT EXISTS td_users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    is_active INT DEFAULT 1
);
CREATE TABLE IF NOT EXISTS td_channels (
    channel_id INT AUTO_INCREMENT PRIMARY KEY,
    channel_name VARCHAR(255) NOT NULL,
    is_active INT DEFAULT 1
);
CREATE TABLE IF NOT EXISTS td_channels_users (
   channel_user_id INT AUTO_INCREMENT PRIMARY KEY,
   channel_id INT NOT NULL,
   user_id INT NOT NULL,
   channel_role VARCHAR(255) NOT NULL,
   is_active INT DEFAULT 1,
   FOREIGN KEY (channel_id) REFERENCES td_channels(channel_id),
   FOREIGN KEY (user_id) REFERENCES td_users(user_id)
);
CREATE TABLE IF NOT EXISTS td_chats (
   chat_id INT AUTO_INCREMENT PRIMARY KEY,
   channel_id INT,
   user_friend_id INT,
   is_active INT DEFAULT 1,
   FOREIGN KEY (channel_id) REFERENCES td_channels(channel_id)
);
CREATE TABLE IF NOT EXISTS td_user_friends (
     user_friend_id INT AUTO_INCREMENT PRIMARY KEY,
     f_user_id INT NOT NULL,
     friend_id INT NOT NULL,
     chat_id INT,
     FOREIGN KEY (f_user_id) REFERENCES td_users(user_id),
     FOREIGN KEY (friend_id) REFERENCES td_users(user_id),
     FOREIGN KEY (chat_id) REFERENCES td_chats(chat_id),
     is_active INT DEFAULT 1
);
CREATE TABLE IF NOT EXISTS td_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    message_content VARCHAR(255),
    user_id INT NOT NULL,
    chat_id INT NOT NULL,
    is_active INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES td_users(user_id),
    FOREIGN KEY (chat_id) REFERENCES td_chats(chat_id)
);

