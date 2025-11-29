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

/**
 * Fragment for admins to view a list of upcoming events within a year.
 * Includes a back button to return to the previous screen.
 */
public class AdminEventFragment extends Fragment {

    private Button backButton;
    private RecyclerView adminEventView;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.admin_event_page, container, false);

        // Initialize UI components
        adminEventView = view.findViewById(R.id.adminEventView);
        backButton = view.findViewById(R.id.buttonBack);

        // Initialize data structures and adapter
        eventsArrayList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        adminEventView.setLayoutManager(new LinearLayoutManager(getContext()));
        adminEventView.setAdapter(eventArrayAdapter);

        eventService = new EventService();

        // Load upcoming events and set click listener for back button
        loadEvents();
        setUpClickListener();

        return view;
    }

    /**
     * Loads upcoming events from today to one year in the future.
     * Uses DateValidation to ensure the date range is valid.
     */
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
                        // Optional: handle failure (log or Toast)
                    });
        }
    }

    /**
     * Sets up click listeners for UI components.
     * Currently only the back button.
     */
    private void setUpClickListener() {
        backButton.setOnClickListener(v -> NavHostFragment.findNavController(AdminEventFragment.this)
                .navigateUp());
    }
}
