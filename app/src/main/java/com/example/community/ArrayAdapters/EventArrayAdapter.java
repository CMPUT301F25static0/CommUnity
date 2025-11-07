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

import java.util.ArrayList;
import java.util.List;

public class EventArrayAdapter extends RecyclerView.Adapter<EventArrayAdapter.ViewHolder> {
    private List<Event> events;
    public static TextView eventName;
    public static TextView eventDescription;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            eventName = view.findViewById(R.id.event_name);
            eventDescription = view.findViewById(R.id.event_description);

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
        eventName.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
    }
    @Override
    public int getItemCount() {
        return events.size();
    }
}


