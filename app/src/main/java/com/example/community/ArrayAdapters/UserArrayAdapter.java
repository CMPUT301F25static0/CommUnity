package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.User;

import java.util.List;

public class UserArrayAdapter extends RecyclerView.Adapter<UserArrayAdapter.ViewHolder> {
    private List<User> users;



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameInList);
        }
    }

    public UserArrayAdapter(List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_user_lists_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        String displayName = (user.getUsername() != null && !user.getUsername().isEmpty())
                ? user.getUsername()
                : user.getUserID();
        holder.userNameTextView.setText(displayName);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
