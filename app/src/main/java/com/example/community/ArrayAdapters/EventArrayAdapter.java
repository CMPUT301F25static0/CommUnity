package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.Event;
import com.example.community.R;

import java.util.List;

public class EventArrayAdapter extends RecyclerView.Adapter<EventArrayAdapter.ViewHolder> {

    private final List<Event> events;
    private final boolean isAdminOrHost;
    private OnItemClickListener listener;

    // Interface to handle clicks in the Fragment
    public interface OnItemClickListener {
        void onItemClick(Event event);
        void onDeleteClick(Event event, int position);
    }

    public EventArrayAdapter(List<Event> events, boolean isAdminOrHost) {
        this.events = events;
        this.isAdminOrHost = isAdminOrHost;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventName;
        public final TextView eventDescription;
        public final Button deleteButton;

        public ViewHolder(View view, boolean isAdminOrHost) {
            super(view);
            eventName = view.findViewById(R.id.event_name);
            eventDescription = view.findViewById(R.id.event_description);
            deleteButton = view.findViewById(R.id.buttonRemove);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId;
        if (isAdminOrHost) {
            layoutId = R.layout.admin_event;
        } else {
            layoutId = R.layout.entrant_event_list_content;
        }
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutId, viewGroup, false);
        return new ViewHolder(view, isAdminOrHost);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);

        // bind data to views
        holder.eventName.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());

        // This handles clicking the entire row
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(event);
            }
        });

        // Handle delete button if it exists
        if (isAdminOrHost && holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(event, holder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
