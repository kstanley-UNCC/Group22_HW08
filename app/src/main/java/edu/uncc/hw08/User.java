package edu.uncc.hw08;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String displayName;
    private Boolean online = false;

    public User() {}

    public User(FirebaseUser firebaseUser) {
        this.userId = firebaseUser.getUid();
        this.displayName = firebaseUser.getDisplayName();
        this.online = true;
    }

    public User(String userId, String displayName, Boolean online) {
        this.userId = userId;
        this.displayName = displayName;
        this.online = online;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean isOnline() {
        return online;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public User setOnline(Boolean online) {
        this.online = online;
        return this;
    }
}
