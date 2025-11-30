package com.example.community.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.Event;
import com.example.community.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventArrayAdapter extends RecyclerView.Adapter<EventArrayAdapter.ViewHolder> {
    private List<Event> events;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView eventDescription;
        public ImageView eventThumbnail;
        public ViewHolder(View view) {
            super(view);
            eventName = view.findViewById(R.id.event_name);
            eventDescription = view.findViewById(R.id.event_description);
            eventThumbnail = view.findViewById(R.id.event_thumbnail);

            view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEventClick(events.get(position));
                }
            });

        }
    }

    public EventArrayAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.entrant_event_list_content, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Event event = events.get(position);
        viewHolder.eventName.setText(event.getTitle());
        viewHolder.eventDescription.setText(event.getDescription());

        String posterURL = event.getPosterImageURL();
        if (posterURL != null && !posterURL.isEmpty()) {
            Picasso. get()
                    .load(posterURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    . into(viewHolder.eventThumbnail);
        } else {
            // Use default placeholder if no poster URL
            viewHolder.eventThumbnail.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }
}

