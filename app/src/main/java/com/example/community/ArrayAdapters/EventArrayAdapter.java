package com.example.community.ArrayAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.community.Event;

import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View )
}
