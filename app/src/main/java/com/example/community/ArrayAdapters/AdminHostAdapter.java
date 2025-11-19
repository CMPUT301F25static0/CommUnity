package com.example.community.ArrayAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;

import java.util.List;

public class AdminHostAdapter extends RecyclerView.Adapter<AdminHostAdapter.HostViewHolder> {

    private List<User> userList;
    private final UserService userService;
    private final Context context;

    // Interface to handle button clicks in the Fragment
    public interface OnHostListener {
        void onViewClicked(User user);
        void onDeleteClicked(User user, int position);
    }

    private final OnHostListener onHostListener;

    public AdminHostAdapter(Context context, List<User> userList, OnHostListener onHostListener) {
        this.context = context;
        this.userList = userList;
        this.userService = new UserService();
        this.onHostListener = onHostListener;
    }

    @NonNull
    @Override
    public HostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_host_profile, parent, false);
        return new HostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostViewHolder holder, int position) {
        User user = userList.get(position);

        String displayName = (user.getUsername() != null && !user.getUsername().isEmpty())
                ? user.getUsername()
                : user.getUserID();
        holder.hostNameTextView.setText(displayName);

        // Set click listener for the "View" button
        holder.viewButton.setOnClickListener(v -> {
            if (onHostListener != null) {
                onHostListener.onViewClicked(user);
            }
        });

        // Set click listener for the "Delete" button
        holder.deleteButton.setOnClickListener(v -> {
            if (onHostListener != null) {
                onHostListener.onDeleteClicked(user, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder class that holds the views for each item
    public static class HostViewHolder extends RecyclerView.ViewHolder {
        TextView hostNameTextView;
        Button viewButton;
        Button deleteButton;

        public HostViewHolder(@NonNull View itemView) {
            super(itemView);
            hostNameTextView = itemView.findViewById(R.id.hostName);
            viewButton = itemView.findViewById(R.id.buttonView);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
