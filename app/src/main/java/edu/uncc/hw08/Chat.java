// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    public String id;
    public String owner;
    public String ownerName;
    public String receiver;
    public String receiverName;
    public String lastMessage;
    public Timestamp lastSent;
    public List<Message> messages;

    public Chat() {}

    public Chat(String id, String owner, String ownerName, String receiver, String receiverName, String lastMessage, Timestamp lastSent) {
        this.id = id;
        this.owner = owner;
        this.ownerName = ownerName;
        this.receiver = receiver;
        this.receiverName = receiverName;
        this.lastMessage = lastMessage;
        this.lastSent = lastSent;
        this.messages = new ArrayList<>();
    }

    public Chat(String id, String owner, String ownerName, String receiver, String receiverName, String lastMessage, Timestamp lastSent, List<Message> messages) {
        this.id = id;
        this.owner = owner;
        this.ownerName = ownerName;
        this.receiver = receiver;
        this.receiverName = receiverName;
        this.lastMessage = lastMessage;
        this.lastSent = lastSent;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastSent() {
        return lastSent.toDate().toString();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Chat setId(String id) {
        this.id = id;
        return this;
    }

    public Chat setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public Chat setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public Chat setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Chat setReceiverName(String receiverName) {
        this.receiverName = receiverName;
        return this;
    }

    public Chat setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public Chat setLastSent(Timestamp lastSent) {
        this.lastSent = lastSent;
        return this;
    }

    public Chat setMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }
}
