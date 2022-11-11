// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.uncc.hw08.databinding.FragmentCreateChatBinding;

public class CreateChatFragment extends Fragment implements UsersListViewAdapter.UsersListener {

    FragmentCreateChatBinding binding;
    UsersListViewAdapter adapter;
    LinearLayoutManager layoutManager;
    FirebaseUser chosenUser;

    private static final String ARG_USER = "user";

    private FirebaseUser firebaseUser;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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
        RecyclerView usersListRecyclerView = view.findViewById(R.id.listViewUsers);
        usersListRecyclerView.setHasFixedSize(true);


      //  if (chosenUser == null) {
            binding.textViewSelectedUser.setText(R.string.no_user_selected);
     //   } else {
           // binding.textViewSelectedUser.setText(chosenUser.getUid());
      //  }

        binding.buttonCancel.setOnClickListener(v -> mListener.gotoMyChats());

        binding.buttonSubmit.setOnClickListener(v -> {
            String chatText = binding.editTextMessage.getText().toString();
            mListener.createChat(chatText, chosenUser);
        });

        String userId = firebaseUser.getUid();

        Query query = firebaseFirestore
                .collection("Users")
                .orderBy(userId);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        layoutManager = new LinearLayoutManager(getActivity());
        usersListRecyclerView.setLayoutManager(layoutManager);
        adapter = new UsersListViewAdapter(getActivity(), firebaseFirestore, this);
        usersListRecyclerView.setAdapter(adapter);

    }

    CreateChatListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateChatListener) context;
    }

    @Override
    public void userClicked(FirebaseUser firebaseUser) {
        chosenUser = firebaseUser;
    }

    interface CreateChatListener {
        void gotoMyChats();
        void createChat(String chatText, FirebaseUser chosenUser);
    }
}