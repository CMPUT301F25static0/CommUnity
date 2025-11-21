package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.EntryStatus;
import com.example.community.R;
import com.example.community.User;
import com.example.community.WaitingListEntry;

import org.w3c.dom.Text;

import java.util.List;

public class UserArrayAdapter extends RecyclerView.Adapter<UserArrayAdapter.ViewHolder> {
    private List<User> users;
    private List<WaitingListEntry> entries;
    private boolean showEntryStatus;



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userNameTextView;
        TextView userEmailTextView;
        TextView userPhoneNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameInList);
            userEmailTextView = itemView.findViewById(R.id.userEmailInList);
            userPhoneNumberTextView = itemView.findViewById(R.id.userPhoneInList);
        }
    }

    public UserArrayAdapter(List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organizer_event_user_lists_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        String displayName = user.getUsername();
        holder.userNameTextView.setText(displayName);
        holder.userEmailTextView.setText(user.getEmail());

        String phone = (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty())
                ? "Phone: " + user.getPhoneNumber()
                : "Phone: No phone number provided";
        holder.userPhoneNumberTextView.setText(phone);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
