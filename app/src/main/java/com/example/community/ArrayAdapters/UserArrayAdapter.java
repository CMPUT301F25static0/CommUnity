package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.User;
import com.example.community.WaitingListEntry;
import com.example.community.R;

import java.util.List;

/**
 * RecyclerView Adapter for displaying a list of {@link User} objects.
 * Shows each user's name, email, and phone number.
 */
public class UserArrayAdapter extends RecyclerView.Adapter<UserArrayAdapter.ViewHolder> {

    /** List of users to display in the RecyclerView */
    private List<User> users;

    /** Optional list of waiting list entries associated with users (unused currently) */
    private List<WaitingListEntry> entries;

    /** Flag indicating whether to show entry status (unused currently) */
    private boolean showEntryStatus;

    /**
     * ViewHolder class for caching references to the views in each user list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** TextView displaying the user's name */
        TextView userNameTextView;

        /** TextView displaying the user's email */
        TextView userEmailTextView;

        /** TextView displaying the user's phone number */
        TextView userPhoneNumberTextView;

        /**
         * Constructor for ViewHolder.
         *
         * @param itemView Root view of the RecyclerView item.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameInList);
            userEmailTextView = itemView.findViewById(R.id.userEmailInList);
            userPhoneNumberTextView = itemView.findViewById(R.id.userPhoneInList);
        }
    }

    /**
     * Constructor for UserArrayAdapter.
     *
     * @param users List of User objects to display.
     */
    public UserArrayAdapter(List<User> users){
        this.users = users;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder}.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organizer_event_user_lists_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display data at the specified position.
     * Populates the user's name, email, and phone number.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position Position of the item in the users list.
     */
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

    /**
     * Returns the total number of users in the adapter.
     *
     * @return Size of the users list.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }
}
