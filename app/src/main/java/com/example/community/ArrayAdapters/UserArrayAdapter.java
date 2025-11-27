package com.example.community.ArrayAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.User;
import com.example.community.WaitingListEntry;

import java.util.List;

public class UserArrayAdapter extends RecyclerView.Adapter<UserArrayAdapter.ViewHolder> {
    private static final String TAG = "UserArrayAdapter";
    private List<User> users;
    private String listType;
    private OnUserSelectionListener selectionListener;



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userNameTextView;
        TextView userEmailTextView;
        TextView userPhoneNumberTextView;
        CheckBox userSelectionCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameInList);
            userEmailTextView = itemView.findViewById(R.id.userEmailInList);
            userPhoneNumberTextView = itemView.findViewById(R.id.userPhoneInList);
            userSelectionCheckbox = itemView.findViewById(R.id.userSelectionCheckbox);
        }
    }

    public UserArrayAdapter(List<User> users, String listType){
        this.users = users;
        this.listType = listType;
    }

    public void setSelectionListener(OnUserSelectionListener listener){
        this.selectionListener = listener;
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

        Log.d(TAG, "onBindViewHolder: listType = " + listType);

        holder.userSelectionCheckbox.setOnCheckedChangeListener(null);
        holder.userSelectionCheckbox.setChecked(false);

        if ("invited".equals(listType)) {
            holder.userSelectionCheckbox.setVisibility(View. VISIBLE);
            holder.userSelectionCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Log.d(TAG, "Checkbox changed for user:  " + user.getUserID() + " isChecked = " + isChecked);
                if (selectionListener != null) {
                    selectionListener.onUserSelectionChanged(user.getUserID(), isChecked);
                }
            });
        } else {
            Log.d(TAG, "Checkbox hidden for user:  " + user.getUserID());
            holder.userSelectionCheckbox.setVisibility(View. GONE);
            holder.userSelectionCheckbox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnUserSelectionListener {
        void onUserSelectionChanged(String userId, boolean selected);
    }
}
