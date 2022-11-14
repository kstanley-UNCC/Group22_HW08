// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements MyChatsFragment.MyChatsFragmentListener, CreateChatFragment.CreateChatListener, ChatFragment.iListener {
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = getIntent().getParcelableExtra("user");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new MyChatsFragment())
                .commit();
    }

    @Override
    public void goToChat(Chat chat) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ChatFragment(chat))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goCreateChat() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateChatFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        Map<String, Object> data = new HashMap<>();
        data.put("online", false);

        // To keep things responsive, we intentionally ignore the response.
        firebaseFirestore
                .collection("Users")
                .document(currentUser.getUserId())
                .update(data);

        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void gotoMyChats() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void createChat(String chatText, User chosenUser, FirebaseUser currentUser) {
        Message message = new Message(
            UUID.randomUUID().toString(),
            currentUser.getDisplayName(),
            chatText,
            Timestamp.now()
        );

        Chat chat = new Chat(
            UUID.randomUUID().toString(),
            currentUser.getUid(),
            currentUser.getDisplayName(),
            chosenUser.getUserId(),
            chosenUser.getDisplayName(),
            "",
            Timestamp.now()
        );

        // Create the chat document first
        Map<String, Object> data = new HashMap<>();
        data.put("id", chat.getId());
        data.put("owner", chat.getOwner());
        data.put("ownerName", chat.getOwnerName());
        data.put("receiver", chat.getReceiver());
        data.put("receiverName", chat.getReceiverName());
        data.put("lastMessage", chat.getLastMessage());
        data.put("lastSent", chat.lastSent);

        firebaseFirestore
            .collection("Chats")
            .document(chat.getId())
            .set(data)
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Exception exception = task.getException();
                    assert exception != null;
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("An Error Occurred")
                            .setMessage(exception.getLocalizedMessage())
                            .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                            .show();
                    return;
                }

                // Step 2 is to create/update the messages collection
                data.clear();
                data.put("id", message.getId());
                data.put("from", message.getFrom());
                data.put("sent", message.sent);
                data.put("message", message.getMessage());

                firebaseFirestore
                    .collection("Chats")
                    .document(chat.getId())
                    .collection("Messages")
                    .document(message.getId())
                    .set(data)
                    .addOnCompleteListener(task1 -> {
                        if (!task1.isSuccessful()) {
                            Exception exception = task.getException();
                            assert exception != null;
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("An Error Occurred")
                                    .setMessage(exception.getLocalizedMessage())
                                    .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                                    .show();
                            return;
                        }

                        goToChat(chat);
                    });
            });
    }
}