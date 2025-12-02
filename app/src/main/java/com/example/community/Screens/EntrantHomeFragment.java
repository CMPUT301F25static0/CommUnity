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

public class EntrantHomeFragment extends Fragment {

    ImageButton entrantNotificationsButton, entrantQRScannerButton;
    Button entrantFilterButton, eventHistoryButton, myProfileButton, guideButton;
    RecyclerView entrantEventList;

    // Lists
    private ArrayList<Event> eventsArrayList;      // currently displayed (possibly filtered)
    private ArrayList<Event> allEventsArrayList;   // master list of all events

    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    // Current filters
    private String currentFilterKeyword = "";
    private String currentFilterTime = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View entrantHomeFragment = inflater.inflate(R.layout.user_events, container, false);
        return entrantHomeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        entrantNotificationsButton = view.findViewById(R.id.goToNotifications);
        entrantQRScannerButton = view.findViewById(R.id.entrantQRScanner);
        entrantFilterButton = view.findViewById(R.id.filterButton);
        eventHistoryButton = view.findViewById(R.id.event_history);
        myProfileButton = view.findViewById(R.id.my_profile);
        entrantEventList = view.findViewById(R.id.event_list);
        guideButton = view.findViewById(R.id.guideButton);

        eventService = new EventService();
        eventsArrayList = new ArrayList<>();
        allEventsArrayList = new ArrayList<>();

        entrantEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        eventArrayAdapter.setOnEventClickListener(event -> {
            Bundle args = new Bundle();
            args.putString("event_id", event.getEventID());
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EventDescriptionFragment, args);

        });
        entrantEventList.setAdapter(eventArrayAdapter);

        loadEvents();
        setUpClickListener();
        setUpFilterResultListener();
    }

    private void loadEvents() {
        String fromDate = DateValidation.getCurrentDate();

        LocalDate futureDate = LocalDate.now().plusYears(1);
        String toDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (DateValidation.dateRangeValid(fromDate, toDate)) {
            Log.d("EntrantHomeFragment", "Loading events from " + fromDate + " to " + toDate);
            eventService.listUpcoming(fromDate, toDate, null)
                    .addOnSuccessListener(events -> {
                        Log.d("EntrantHomeFragment", "Loaded " + events.size() + " events");

                        // Save all events to master list
                        allEventsArrayList.clear();
                        allEventsArrayList.addAll(events);

                        // If no active filter → show everything
                        if (currentFilterKeyword.isEmpty() && currentFilterTime.isEmpty()) {
                            eventsArrayList.clear();
                            eventsArrayList.addAll(allEventsArrayList);
                            eventArrayAdapter.notifyDataSetChanged();
                        } else {
                            // Re-apply the current filter on the fresh data
                            applyFilters(currentFilterKeyword, currentFilterTime);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("EntrantHomeFragment", "Failed to load events", e);
                        Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void setUpClickListener() {
        entrantNotificationsButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_NotificationsFragment);
        });

        myProfileButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EntrantUserProfileFragment);
        });

        eventHistoryButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.UserEventHistoryFragment);
        });

        entrantFilterButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                        .navigate(R.id.action_EntrantHomeFragment_to_UserFilterFragment)
        );

        entrantQRScannerButton.setOnClickListener(v -> {
            Log.d("EntrantHomeFragment", "QR Scanner button clicked");
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_QRScannerFragment);
        });

        guideButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                        .navigate(R.id.UserGuideFragment)
        );
    }

    private void setUpFilterResultListener() {
        var navController = NavHostFragment.findNavController(EntrantHomeFragment.this);

        if (navController.getCurrentBackStackEntry() == null) {
            Log.w("EntrantHomeFragment", "No currentBackStackEntry for filter result");
            return;
        }

        navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("eventFilters")
                .observe(getViewLifecycleOwner(), value -> {
                    if (value instanceof Bundle) {
                        Bundle filters = (Bundle) value;
                        String keyword = filters.getString("keyword", "");
                        String time = filters.getString("time", "");

                        Log.d("EntrantHomeFragment", "Received filters: keyword=" + keyword + ", time=" + time);
                        applyFilters(keyword, time);
                    } else {
                        Log.w("EntrantHomeFragment", "Filter value is not a Bundle");
                    }
                });
    }

    private void applyFilters(String keyword, String time) {
        // Remember current filters so loadEvents() can re-apply them after async fetch
        currentFilterKeyword = (keyword == null) ? "" : keyword;
        currentFilterTime = (time == null) ? "" : time;

        if (allEventsArrayList == null || allEventsArrayList.isEmpty()) {
            Log.d("EntrantHomeFragment", "applyFilters: events not loaded yet, will apply after loadEvents");
            return; // loadEvents() will call applyFilters again once data arrives
        }

        String keywordLower = currentFilterKeyword.toLowerCase();
        String timeLower = currentFilterTime.toLowerCase();

        // If both empty → show all events again
        if (keywordLower.isEmpty() && timeLower.isEmpty()) {
            eventsArrayList.clear();
            eventsArrayList.addAll(allEventsArrayList);
            eventArrayAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(),
                    "Cleared filter. Showing all events (" + eventsArrayList.size() + ")",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        eventsArrayList.clear();

        for (Event e : allEventsArrayList) {
            if (e == null) continue;

            boolean matchesKeyword = true;
            boolean matchesTime = true;

            if (!keywordLower.isEmpty()) {
                String title = safeLower(e.getTitle());
                String description = safeLower(e.getDescription());
                String location = safeLower(e.getLocation());

                matchesKeyword =
                        title.contains(keywordLower)
                                || description.contains(keywordLower)
                                || location.contains(keywordLower);
            }

            if (!timeLower.isEmpty()) {
                String start = safeLower(e.getEventStartDate());
                String end = safeLower(e.getEventEndDate());

                matchesTime = start.contains(timeLower) || end.contains(timeLower);
            }

            if (matchesKeyword && matchesTime) {
                eventsArrayList.add(e);
            }
        }

        Log.d("EntrantHomeFragment", "Filtered events count = " + eventsArrayList.size());
        eventArrayAdapter.notifyDataSetChanged();

        Toast.makeText(getContext(),
                "Applied filter. Showing " + eventsArrayList.size() + " events.",
                Toast.LENGTH_SHORT).show();
    }

    private String safeLower(String s) {
        return (s == null) ? "" : s.toLowerCase();
    }
}
