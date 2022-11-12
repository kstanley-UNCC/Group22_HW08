package edu.uncc.hw08;

import androidx.annotation.NonNull;

import java.util.UUID;

public class User {
    public String userName;
    public String userId = UUID.randomUUID().toString();
    public boolean onlineStatus;

    public User() {}

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", onlineStatus=" + onlineStatus +
                '}';
    }
}
