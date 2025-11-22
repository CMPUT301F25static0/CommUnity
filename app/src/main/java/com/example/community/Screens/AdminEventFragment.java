package com.example.community.Screens;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.EventArrayAdapter;
import com.example.community.DateValidation;
import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// 1. Implement the interface exactly as defined in your Adapter
public class AdminEventFragment extends Fragment implements EventArrayAdapter.OnEventClickListener {

    Button backButton;
    RecyclerView adminEventView;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_event_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        adminEventView = view.findViewById(R.id.adminEventView);
        backButton = view.findViewById(R.id.buttonBack);

        // Initialize Service and List
        eventService = new EventService();
        eventsArrayList = new ArrayList<>();

        // Initialize RecyclerView
        adminEventView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. Initialize adapter using your specific constructor (List only)
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);

        // 3. Set the listener
        eventArrayAdapter.setOnEventClickListener(this);

        adminEventView.setAdapter(eventArrayAdapter);

        loadEvents();
        setUpClickListener();
    }

    private void loadEvents() {
        String fromDate = DateValidation.getCurrentDate();

        LocalDate futureDate = LocalDate.now().plusYears(1);
        String toDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (DateValidation.dateRangeValid(fromDate, toDate)) {
            eventService.listUpcoming(fromDate, toDate, null)
                    .addOnSuccessListener(events -> {
                        eventsArrayList.clear();
                        eventsArrayList.addAll(events);
                        eventArrayAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading events", e);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // 4. Handle "View" Click (Single Tap)
    @Override
    public void onEventClick(Event event) {
        // Since your adapter lacks a delete button, we can offer a choice dialog here
        // or just navigate to details. Let's offer a choice for Admin.
        showActionDialog(event);
    }

    private void showActionDialog(Event event) {
        CharSequence[] options = new CharSequence[]{"View Event", "Delete Event"};

        new AlertDialog.Builder(requireContext())
                .setTitle(event.getTitle())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // View Event
                        navigateToUpdatePage(event);
                    } else {
                        // Delete Event
                        confirmDelete(event);
                    }
                })
                .show();
    }

    private void navigateToUpdatePage(Event event) {
        Bundle bundle = new Bundle();
        bundle.putString("eventID", event.getEventID());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_AdminEventFragment_to_hostPosterUpdatePageFragment, bundle);
    }

    private void confirmDelete(Event event) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete " + event.getTitle() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteEvent(event))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteEvent(Event event) {
        eventService.cancelEvent(event.getOrganizerID(), event.getEventID())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int position = eventsArrayList.indexOf(event);
                        if (position != -1) {
                            eventsArrayList.remove(position);
                            eventArrayAdapter.notifyItemRemoved(position);
                            eventArrayAdapter.notifyItemRangeChanged(position, eventsArrayList.size() - position);
                        }
                        Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpClickListener() {
        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminEventFragment.this)
                    .navigateUp();
        });
    }
}
