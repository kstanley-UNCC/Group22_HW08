package edu.uncc.hw08;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User implements Parcelable {
    private FirebaseUser firebaseUser;
    private Boolean onlineStatus = false;

    public User(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    protected User(Parcel in) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        onlineStatus = in.readByte() == 1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public String userName() {
        return firebaseUser.getEmail();
    }

    public String userId() {
        return firebaseUser.getUid();
    }

    public Boolean isOnline() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(onlineStatus ? 1 : 0);
    }
}
