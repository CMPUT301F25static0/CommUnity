package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.community.Notification;
import com.example.community.R;
import java.util.List;

public class NotificationArrayAdapter extends RecyclerView.Adapter<NotificationArrayAdapter.ViewHolder> {
    private List<Notification> notifications;

    public NotificationArrayAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notif = notifications.get(position);
        holder.message.setText(notif.getMessage());
    }

    @Override
    public int getItemCount() { return notifications.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.notificationLog);
        }
    }
}
