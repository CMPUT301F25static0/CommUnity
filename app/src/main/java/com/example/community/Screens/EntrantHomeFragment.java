package com.example.community.Screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
 * Fragment representing the home screen for an entrant.
 * Displays upcoming events in a RecyclerView and provides navigation
 * to notifications, user profile, event history, QR scanner, filters, and guide pages.
 */
public class EntrantHomeFragment extends Fragment {

    private ImageButton entrantNotificationsButton, entrantQRScannerButton;
    private Button entrantFilterButton, eventHistoryButton, myProfileButton, guideButton;
    private RecyclerView entrantEventList;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    /**
     * Inflates the fragment's layout.
     *
     * @param inflater           LayoutInflater object used to inflate views
     * @param container          Parent container for the fragment
     * @param savedInstanceState Saved instance state bundle
     * @return The inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_events, container, false);
    }

    /**
     * Called after the view has been created.
     * Binds UI elements, initializes services, sets up the RecyclerView and event adapter,
     * loads events, and sets up navigation click listeners.
     *
     * @param view               The fragment's view
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI elements
        entrantNotificationsButton = view.findViewById(R.id.goToNotifications);
        entrantQRScannerButton = view.findViewById(R.id.entrantQRScanner);
        entrantFilterButton = view.findViewById(R.id.filterButton);
        eventHistoryButton = view.findViewById(R.id.event_history);
        myProfileButton = view.findViewById(R.id.my_profile);
        entrantEventList = view.findViewById(R.id.event_list);
        guideButton = view.findViewById(R.id.guideButton);

        // Initialize services and data structures
        eventService = new EventService();
        eventsArrayList = new ArrayList<>();

        // Setup RecyclerView with adapter
        entrantEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        eventArrayAdapter.setOnEventClickListener(event -> {
            Bundle args = new Bundle();
            args.putString("event_id", event.getEventID());
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EventDescriptionFragment, args);
        });
        entrantEventList.setAdapter(eventArrayAdapter);

        // Load upcoming events and set up click listeners for navigation
        loadEvents();
        setUpClickListener();
    }

    /**
     * Loads upcoming events for the next year and updates the RecyclerView.
     * Uses {@link EventService} to retrieve events within a valid date range.
     */
    private void loadEvents() {
        String fromDate = DateValidation.getCurrentDate();
        LocalDate futureDate = LocalDate.now().plusYears(1);
        String toDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (DateValidation.dateRangeValid(fromDate, toDate)) {
            Log.d("EntrantHomeFragment", "Loading events from " + fromDate + " to " + toDate);
            eventService.listUpcoming(fromDate, toDate, null)
                    .addOnSuccessListener(events -> {
                        Log.d("EntrantHomeFragment", "Loaded " + events.size() + " events");
                        eventsArrayList.clear();
                        eventsArrayList.addAll(events);
                        eventArrayAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("EntrantHomeFragment", "Failed to load events", e);
                        Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Sets up navigation click listeners for buttons on the entrant home screen.
     * Handles navigation to notifications, user profile, event history, filters, guide, and QR scanner.
     */
    private void setUpClickListener() {
        entrantNotificationsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                        .navigate(R.id.action_EntrantHomeFragment_to_NotificationsFragment)
        );

        eventHistoryButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                        .navigate(R.id.UserEventHistoryFragment)
        );

        myProfileButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                        .navigate(R.id.action_EntrantHomeFragment_to_EntrantUserProfileFragment)
        );

        entrantQRScannerButton.setOnClickListener(v -> {
            Log.d("EntrantHomeFragment", "QR Scanner button clicked");
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_QRScannerFragment);
        });
        entrantFilterButton.setOnClickListener(v -> {
            Toast myToast = Toast.makeText(getActivity(), "Not Implemented yet", Toast.LENGTH_SHORT);
        });
        guideButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                        .navigate(R.id.UserGuideFragment)
        );
    }
}
