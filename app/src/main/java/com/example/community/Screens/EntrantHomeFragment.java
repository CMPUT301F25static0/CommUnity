package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.util.List;

public class EntrantHomeFragment extends Fragment {

    ImageButton entrantNotifications, entrantQRScanner;
    Button entrantFilterButton, eventHistoryButton, myProfileButton;
    RecyclerView entrantEventList;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View entrantHomeFragment = inflater.inflate(R.layout.user_events, container, false);
        return entrantHomeFragment;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        entrantNotifications = view.findViewById(R.id.entrantNotifications);
        entrantQRScanner = view.findViewById(R.id.entrantQRScanner);
        entrantFilterButton = view.findViewById(R.id.filterButton);
        eventHistoryButton = view.findViewById(R.id.event_history);
        myProfileButton = view.findViewById(R.id.my_profile);
        entrantEventList = view.findViewById(R.id.event_list);

        eventService = new EventService();
        eventsArrayList = new ArrayList<>();

        entrantEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        entrantEventList.setAdapter(eventArrayAdapter);


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
                    });
            }
    }

    private void setUpClickListener() {
        entrantNotifications.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_NotificationsFragment);
        });
        eventHistoryButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EntrantHistoryFragment);

        });
        myProfileButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantHomeFragment.this)
                    .navigate(R.id.action_EntrantHomeFragment_to_EntrantUserProfileFragment);

        });
        entrantQRScanner.setOnClickListener(v -> {
            Toast myToast = Toast.makeText(getActivity(), "Not Implemented yet", Toast.LENGTH_SHORT);

            myToast.show();
        });
        entrantFilterButton.setOnClickListener(v -> {
            Toast myToast = Toast.makeText(getActivity(), "Not Implemented yet", Toast.LENGTH_SHORT);

            myToast.show();
        });
    }
}