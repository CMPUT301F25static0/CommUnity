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

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

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

    private void setUpClickListener() {
        entrantNotificationsButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_NotificationsFragment);
        });
//        eventHistoryButton.setOnClickListener(v -> {
//            NavHostFragment.findNavController(EntrantHomeFragment.this)
//                    .navigate(R.id.action_EntrantHomeFragment_to_EntrantHistoryFragment);
//
//        });
        myProfileButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EntrantUserProfileFragment);

        });
        entrantQRScannerButton.setOnClickListener(v -> {
            Toast myToast = Toast.makeText(getActivity(), "Not Implemented yet", Toast.LENGTH_SHORT);

            myToast.show();
        });
        entrantFilterButton.setOnClickListener(v -> {
            Toast myToast = Toast.makeText(getActivity(), "Not Implemented yet", Toast.LENGTH_SHORT);

            myToast.show();
        });
        guideButton.setOnClickListener(v ->
                NavHostFragment.findNavController(EntrantHomeFragment.this)
                      //  .navigate(R.id.UserGuideFragment)
        );
    }
}