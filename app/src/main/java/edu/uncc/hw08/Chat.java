package edu.uncc.hw08;


import com.google.firebase.Timestamp;

import java.util.UUID;

public class Chat {
    public String chat_id = UUID.randomUUID().toString();
    public String user_name;
    public String user_id;
    public String chatText;
    public Timestamp created_at;

    public Chat(){}

    public Chat(String user_name, String user_id, String chatText) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.chatText = chatText;
        this.created_at = Timestamp.now();
    }

    public String getChat_id() {
        return chat_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getChatText() {
        return chatText;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Chat setChat_id(String chat_id) {
        this.chat_id = chat_id;
        return this;
    }

    public Chat setUser_name(String user_name) {
        this.user_name = user_name;
        return this;
    }

    public Chat setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public Chat setChatText(String chatText) {
        this.chatText = chatText;
        return this;
    }

    public Chat setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
        return this;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chat_id='" + chat_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", chatText='" + chatText + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
