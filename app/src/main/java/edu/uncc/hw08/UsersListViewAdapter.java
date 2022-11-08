package edu.uncc.hw08;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.annotations.NonNull;

public class UsersListViewAdapter extends RecyclerView.Adapter<UsersListViewAdapter.UserViewHolder> {

    FirebaseFirestore firebaseFirestore;
    LayoutInflater inflater;
    UsersListener listener;

    public UsersListViewAdapter(Context layout, FirebaseFirestore firebaseFirestore, UsersListener listener) {
        this.inflater = LayoutInflater.from(layout);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewOnline;
        UsersListener listener;

        public UserViewHolder(@NonNull View itemView, UsersListener listener) {
            super(itemView);
            this.listener = listener;

            textViewName = itemView.findViewById(R.id.textViewName);
           // if () {
                imageViewOnline.setVisibility(View.VISIBLE);
          //  }

        }
    }

    public interface UsersListener {

    }
}
