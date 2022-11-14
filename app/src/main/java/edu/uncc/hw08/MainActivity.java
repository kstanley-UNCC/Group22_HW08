// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyChatsFragment.MyChatsFragmentListener, CreateChatFragment.CreateChatListener {
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    User currentUser;

    Map<String, User> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = getIntent().getParcelableExtra("user");

        // Create a listener that will keep track of all the users for the app
        firebaseFirestore
                .collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            userMap.put(doc.getId(), new User(doc.getId(), (String) doc.get("displayName"), (Boolean) doc.get("online")));
                        }
                    }
                });

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
    public void goCreateChat(FirebaseUser firebaseUser) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CreateChatFragment.newInstance(firebaseUser))
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
    public void createChat(String chatText, FirebaseUser chosenUser) {
        Chat chat = new Chat();

        firebaseFirestore
                .collection("Users")
                .document(chat.getUser_id())
                .collection("Chats")
                .document(chat.getChat_id())
                .set(chat)
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
                    gotoMyChats();
                });
    }
}