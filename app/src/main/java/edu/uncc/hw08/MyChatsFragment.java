// Homework Assignment 08
// Group22_HW08
// Stephanie Lee Karp & Ken Stanley

package edu.uncc.hw08;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.units.qual.C;

import edu.uncc.hw08.databinding.FragmentMyChatsBinding;

public class MyChatsFragment extends Fragment {

    FragmentMyChatsBinding binding;
    ArrayAdapter<Chat> adapter;

    private static final String ARG_USER = "user";

    private FirebaseUser firebaseUser;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public MyChatsFragment() {
        // Required empty public constructor
    }

    public static MyChatsFragment newInstance(FirebaseUser firebaseUser) {
        MyChatsFragment fragment = new MyChatsFragment();
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

        Query query = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("Chats")
                .orderBy("created_at", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();

        adapter = new ArrayAdapter<Chat>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        binding.listViewChats.setAdapter(adapter);

        binding.listViewChats.setOnItemClickListener((adapterView, view1, position, l) -> mListener.goToChat(adapter.getItem(position)));

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
}