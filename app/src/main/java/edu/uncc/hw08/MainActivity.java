// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements MyChatsFragment.MyChatsFragmentListener, CreateChatFragment.CreateChatListener {

    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();
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
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.rootView, ChatFragment.newInstance(chat))
//                .addToBackStack(null)
//                .commit();
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
        Intent intent = new Intent(this, AuthActivity.class);
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