package edu.uncc.hw08;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import io.reactivex.rxjava3.annotations.NonNull;

public class UsersListViewAdapter extends RecyclerView.Adapter<UsersListViewAdapter.UserViewHolder> {

    FirebaseFirestore firebaseFirestore;
    LayoutInflater inflater;
    UsersListener listener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;


    public UsersListViewAdapter(Context layout, FirebaseFirestore firebaseFirestore, UsersListener listener) {
        this.inflater = LayoutInflater.from(layout);
        this.firebaseFirestore = firebaseFirestore;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.users_row_item, parent, false);
        return new UserViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.textViewName.setText(firebaseUser.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewOnline;
        UsersListener listener;
        FirebaseFirestore firebaseFirestore;
        FirebaseUser firebaseUser;


        public UserViewHolder(@NonNull View itemView, UsersListener listener) {
            super(itemView);
            this.listener = listener;

            textViewName = itemView.findViewById(R.id.textViewName);
            //if () {
                //imageViewOnline.setVisibility(View.VISIBLE);
            //}
        }
    }

    public interface UsersListener {
        void userClicked(FirebaseUser firebaseUser);
    }
}
