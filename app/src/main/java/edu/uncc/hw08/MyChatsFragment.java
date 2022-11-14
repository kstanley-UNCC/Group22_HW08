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
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uncc.hw08.databinding.FragmentMyChatsBinding;

public class MyChatsFragment extends Fragment {

    FragmentMyChatsBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogout.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            mListener.logout();
        });

        binding.buttonNewChat.setOnClickListener(v -> mListener.goCreateChat());

        String userId = firebaseUser.getUid();

        Map<String, Chat> chats = new HashMap<>();

        // First find all chats where userId is the owner
        firebaseFirestore
                .collection("Chats")
                .whereEqualTo("owner", userId)
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

//                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
//                        chats.put(doc.getId(), new Chat(
//                                doc.getId(),
//                                doc.get("owner", String.class),
//                                doc.get("ownerName", String.class),
//                                doc.get("receiver", String.class),
//                                doc.get("receiverName", String.class),
//                                doc.get("lastMessage", String.class),
//                                doc.get("lastSent", Timestamp.class)
//                        ));
//                    }

                    // Next we get all chats where we are the receiver
                    firebaseFirestore
                            .collection("Chats")
                            .whereEqualTo("receiver", userId)
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    new AlertDialog.Builder(requireContext())
                                            .setTitle("An Error Occurred")
                                            .setMessage(error.getLocalizedMessage())
                                            .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                                            .show();
                                    return;
                                }

                                assert value != null;
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    chats.put(doc.getId(), new Chat(
                                        doc.getId(),
                                        doc.get("owner", String.class),
                                        doc.get("ownerName", String.class),
                                        doc.get("receiver", String.class),
                                        doc.get("receiverName", String.class),
                                        doc.get("lastMessage", String.class),
                                        doc.get("lastSent", Timestamp.class)
                                    ));
                                }

                                binding.listViewChats.setAdapter(new ChatAdapter(
                                        requireContext(),
                                        R.layout.my_chats_list_item,
                                        new ArrayList<>(chats.values())
                                ));
                            });
                });

        binding.listViewChats.setOnItemClickListener((adapterView, view1, position, l) -> {
            ArrayList<Chat> c = new ArrayList<>(chats.values());
            mListener.goToChat(c.get(position));
        });

        requireActivity().setTitle(R.string.my_chats_label);
    }

    MyChatsFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MyChatsFragmentListener) context;
    }

    interface MyChatsFragmentListener {
        void goToChat(Chat chat);

        void goCreateChat();

        void logout();
    }

    public class ChatAdapter extends ArrayAdapter<Chat> {
        public ChatAdapter(@NonNull Context context, int resource, @NonNull List<Chat> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_chats_list_item, parent, false);
            }

            Chat chat = getItem(position);

            TextView textViewMsgBy = convertView.findViewById(R.id.textViewMsgBy);
            TextView textViewMsgText = convertView.findViewById(R.id.textViewMsgText);
            TextView textViewMsgOn = convertView.findViewById(R.id.textViewMsgOn);

            FirebaseUser firebaseUser = mAuth.getCurrentUser();

            textViewMsgBy.setText(firebaseUser != null && firebaseUser.getUid().equals(chat.getOwner()) ? chat.getReceiverName() : chat.getOwnerName());
            textViewMsgText.setText(chat.getLastMessage());
            textViewMsgOn.setText(chat.getLastSent());

            return convertView;
        }
    }
}