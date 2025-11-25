package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.Notification;
import com.example.community.NotificationType;
import com.example.community.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public interface NotificationActionListener {
        void onAccept(Notification notification);
        void onDecline(Notification notification);
        void onViewEvent(Notification notification);
    }

    private List<Notification> notifications = new ArrayList<>();
    private final NotificationActionListener listener;

    public NotificationAdapter(NotificationActionListener listener) {
        this.listener = listener;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_page_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification n = notifications.get(position);

        // Show message as main text
        holder.messageText.setText(n.getMessage());

        // For now, show a generic label on the middle button
        holder.eventButton.setText("View Event");

        // Show/hide Accept/Decline depending on type (WIN typically needs response)
        if (n.getType() == NotificationType.WIN) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.declineButton.setVisibility(View.VISIBLE);
        } else {
            holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setVisibility(View.GONE);
        }

        holder.eventButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewEvent(n);
            } else {
                Toast.makeText(v.getContext(), "View event: " + n.getEventID(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        holder.acceptButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccept(n);
            } else {
                Toast.makeText(v.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.declineButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDecline(n);
            } else {
                Toast.makeText(v.getContext(), "Declined", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        Button eventButton;
        Button acceptButton;
        Button declineButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText  = itemView.findViewById(R.id.popup_text);
            eventButton  = itemView.findViewById(R.id.studyGroup);
            acceptButton = itemView.findViewById(R.id.accept_button);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}

