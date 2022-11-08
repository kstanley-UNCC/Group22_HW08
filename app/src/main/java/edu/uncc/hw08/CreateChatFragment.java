// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;

import edu.uncc.hw08.databinding.FragmentCreateChatBinding;

public class CreateChatFragment extends Fragment {

    FragmentCreateChatBinding binding;

    private static final String ARG_USER = "user";

    private FirebaseUser firebaseUser;

    public CreateChatFragment() {
        // Required empty public constructor
    }

    public static CreateChatFragment newInstance(FirebaseUser firebaseUser) {
        CreateChatFragment fragment = new CreateChatFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, firebaseUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firebaseUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser chosenUser;

      //  if (chosenUser == null) {
            binding.textViewSelectedUser.setText(R.string.no_user_selected);
     //   } else {
           // binding.textViewSelectedUser.setText(chosenUser.getUid());
      //  }

        binding.buttonCancel.setOnClickListener(v -> mListener.gotoMyChats());

        binding.buttonSubmit.setOnClickListener(v -> {
            String chatText = binding.editTextMessage.getText().toString();
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
        void createChat();
    }
}