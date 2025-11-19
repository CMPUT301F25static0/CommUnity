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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

// 1. Implement the interface from your Adapter
public class AdminEventFragment extends Fragment implements EventArrayAdapter.OnItemClickListener {

    Button backButton;
    RecyclerView adminEventView;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_event_page, container, false);

        // Initialize Views
        adminEventView = view.findViewById(R.id.adminEventView);
        backButton = view.findViewById(R.id.buttonBack);

        // Initialize Navigation
        navController = Navigation.findNavController(container); // Use container to find controller early if needed, or view in onViewCreated

        // Initialize Service and List
        eventService = new EventService();
        eventsArrayList = new ArrayList<>();

        // Initialize RecyclerView Layout Manager
        adminEventView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventArrayAdapter = new EventArrayAdapter(eventsArrayList, true);
        eventArrayAdapter.setOnItemClickListener(this);
        adminEventView.setAdapter(eventArrayAdapter);

        loadEvents();
        setUpClickListener();

        return view;
    }

    private void loadEvents() {
        String fromDate = DateValidation.getCurrentDate();

        LocalDate futureDate = LocalDate.now().plusYears(1);
        String toDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (DateValidation.dateRangeValid(fromDate, toDate)) {
            // Assuming 'listUpcoming' returns a Task<List<Event>>
            eventService.listUpcoming(fromDate, toDate, null)
                    .addOnSuccessListener(events -> {
                        eventsArrayList.clear();
                        eventsArrayList.addAll(events);
                        eventArrayAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading events", e);
                        Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // 2. Handle the "View" Click
    @Override
    public void onItemClick(Event event) {
        Toast.makeText(getContext(), "Viewing event: " + event.getTitle(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("eventID", event.getEventID());
        navController.navigate(R.id.action_AdminEventFragment_to_hostPosterUpdatePageFragment, bundle);
    }

    // 3. Handle the "Delete" Click
    @Override
    public void onDeleteClick(Event event, int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete " + event.getTitle() + "? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {

                    // Assuming eventService has a delete method (e.g., deleteEvent or cancelEvent)
                    // Check your EventService.java to confirm the method name!
                    eventService.cancelEvent(event.getOrganizerID(),event.getEventID()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Event deleted successfully: " + event.getEventID());

                            // Remove from local list
                            eventsArrayList.remove(position);

                            // Notify Adapter
                            eventArrayAdapter.notifyItemRemoved(position);
                            // Fix: Use eventsArrayList, not userList
                            if (position < eventsArrayList.size()) {
                                eventArrayAdapter.notifyItemRangeChanged(position, eventsArrayList.size() - position);
                            }

                            Toast.makeText(getContext(), "Event deleted.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to delete event", task.getException());
                            Toast.makeText(getContext(), "Failed to delete event.", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setUpClickListener() {
        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminEventFragment.this)
                    .navigateUp();
        });
    }
}
