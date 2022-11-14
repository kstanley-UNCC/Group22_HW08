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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.uncc.hw08.databinding.FragmentChatBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    Chat chat;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirestoreRecyclerAdapter<Message, MessageHolder> adapter;

    public ChatFragment(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentChatBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        requireActivity().setTitle(getString(R.string.chat_label, firebaseUser.getUid().equals(chat.getOwner()) ? chat.getReceiverName() : chat.getOwnerName()));

        binding.buttonDeleteChat.setOnClickListener(v -> firebaseFirestore
                .collection("Chats")
                .document(chat.getId())
                .delete()
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

                    mListener.gotoMyChats();
                }));

        binding.buttonClose.setOnClickListener(v -> mListener.gotoMyChats());

        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(requireContext()));

        Query query = firebaseFirestore
                .collection("Chats")
                .document(chat.getId())
                .collection("Messages");

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Message, MessageHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
                holder.setId(model.getId());
                holder.setFrom(model.getFrom());
                holder.setSent(model.getSent());
                holder.setMessage(model.getMessage());
            }

            @NonNull
            @Override
            public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
                return new MessageHolder(view);
            }
        };
        binding.recyclerViewMessages.setAdapter(adapter);

        binding.buttonSubmit.setOnClickListener(v -> {
            String chatMessage = binding.editTextMessage.getText().toString();
            mListener.addMessage(chatMessage, chat, firebaseUser);
        });
    }

    iListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (iListener) context;
    }

    public interface iListener {
        void gotoMyChats();
        void addMessage(String chatText, Chat chat, FirebaseUser firebaseUser);
    }

    private class MessageHolder extends RecyclerView.ViewHolder {
        private final View view;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        void setFrom(String from) {
            TextView textView = view.findViewById(R.id.textViewMsgBy);
            textView.setText(from);
        }

        void setSent(String sent) {
            TextView textView = view.findViewById(R.id.textViewMsgOn);
            textView.setText(sent);
        }

        void setMessage(String message) {
            TextView textView = view.findViewById(R.id.textViewMsgText);
            textView.setText(message);
        }

        public void setId(String id) {
            ImageView imageViewDelete = view.findViewById(R.id.imageViewDelete);
            imageViewDelete.setOnClickListener(view -> firebaseFirestore
                    .collection("Chats")
                    .document(chat.getId())
                    .collection("Messages")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(unused -> Log.d("demo", "Message successfully deleted"))
                    .addOnFailureListener(e -> Log.w("demo", "Error deleting message", e)));
        }
    }
}