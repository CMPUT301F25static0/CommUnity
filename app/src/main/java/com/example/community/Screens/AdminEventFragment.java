package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.EventArrayAdapter;
import com.example.community.DateValidation;
import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminEventFragment extends Fragment {

    Button backButton;

    RecyclerView adminEventView;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_event_page, container, false);

        adminEventView = view.findViewById(R.id.adminEventView);
        adminEventView.setAdapter(eventArrayAdapter);

        eventsArrayList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        adminEventView.setAdapter(eventArrayAdapter);

        db = FirebaseFirestore.getInstance();

        fetchEvents();

        return view;
    }

    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventsArrayList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            eventsArrayList.add(event);
                        }
                        eventArrayAdapter.notifyDataSetChanged();
                    } else {

                    }

                });
    }
}
