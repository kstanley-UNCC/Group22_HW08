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
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mAuth.getCurrentUser() == null){
            setContentView(R.layout.activity_auth);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void authenticate(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                assert exception != null;
                new AlertDialog.Builder(AuthActivity.this)
                        .setTitle("An Error Occurred")
                        .setMessage(exception.getLocalizedMessage())
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            this.firebaseUser = task.getResult().getUser();

            gotoMyChats(this.firebaseUser);
        });
    }

    @Override
    public void createAccount(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(createTask -> {
            if (!createTask.isSuccessful()) {
                Exception exception = createTask.getException();
                assert exception != null;
                new AlertDialog.Builder(AuthActivity.this)
                        .setTitle("An Error Occurred")
                        .setMessage(exception.getLocalizedMessage())
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            FirebaseUser user = createTask.getResult().getUser();
            assert user != null;
            user.updateProfile(request).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Exception exception = task.getException();
                    assert exception != null;
                    new AlertDialog.Builder(AuthActivity.this)
                            .setTitle("An Error Occurred")
                            .setMessage(exception.getLocalizedMessage())
                            .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                            .show();
                    return;
                }

                this.firebaseUser = user;

                gotoMyChats(user);
            });
        });
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoSignUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .commit();
    }

    public void gotoMyChats(FirebaseUser firebaseUser) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, MyChatsFragment.newInstance(firebaseUser))
                .commit();
    }
}