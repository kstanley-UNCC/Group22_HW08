// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uncc.hw08.databinding.FragmentChatBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    Chat chat;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (chat.getReceiver() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            assert firebaseUser != null;

            requireActivity().setTitle(getString(R.string.chat_label, firebaseUser.getUid().equals(chat.getOwner()) ? chat.getReceiverName() : chat.getOwnerName()));
        } else {
            requireActivity().setTitle(R.string.new_chat_label);
        }
    }
}