// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uncc.hw08.databinding.FragmentCreateChatBinding;

public class CreateChatFragment extends Fragment {

    FragmentCreateChatBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();
    User chosenUser;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(v -> mListener.gotoMyChats());

        Map<String, User> users = new HashMap<>();

        firebaseFirestore
                .collection("Users")
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        assert exception != null;
                        new AlertDialog.Builder(requireContext())
                                .setTitle("An Error Occurred")
                                .setMessage(exception.getLocalizedMessage())
                                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                                .show();
                        return;
                    }

                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        users.put(doc.getId(), new User(
                                doc.getId(),
                                doc.get("displayName", String.class),
                                doc.get("online", Boolean.class)
                        ));
                    }

                    binding.listViewUsers.setAdapter(new UserAdapter(
                            requireContext(),
                            R.layout.users_row_item,
                            new ArrayList<>(users.values())
                    ));
                });

        binding.listViewUsers.setOnItemClickListener((adapterView, view1, position, l) -> {
            ArrayList<User> u = new ArrayList<>(users.values());
            chosenUser = u.get(position);

            binding.textViewSelectedUser.setText(chosenUser.getDisplayName());

            binding.buttonSubmit.setOnClickListener(v -> {
                String chatText = binding.editTextMessage.getText().toString();
                mListener.createChat(chatText, chosenUser, firebaseUser);
            });
        });
    }

    CreateChatListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateChatListener) context;
    }

    interface CreateChatListener {
        void gotoMyChats();
        void createChat(String chatText, User chosenUser, FirebaseUser firebaseUser);
    }

    public class UserAdapter extends ArrayAdapter<User> {

        public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.users_row_item, parent, false);
            }

            User user = getItem(position);

            TextView textViewName = convertView.findViewById(R.id.textViewName);
            ImageView imageViewOnline = convertView.findViewById(R.id.imageViewOnline);

            textViewName.setText(user.getDisplayName());

            if (user.isOnline()) {
                imageViewOnline.setVisibility(View.VISIBLE);
            } else {
                imageViewOnline.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }
}