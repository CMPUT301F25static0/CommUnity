package com.example.community.Screens;

import android.os.Bundle;
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

public class OrganizerHomeFragment extends Fragment {
    ImageButton notificationsButton, cameraButton;
    Button guideButton, filterButton, createButton, notifyButton;
    Button eventHistoryButton, myProfileButton;
    RecyclerView hostEventList;

    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View organizerHomeFragment = inflater.inflate(R.layout.host_main_page, container, false);
        return organizerHomeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventService = new EventService();
        eventsArrayList = new ArrayList<>();


        notificationsButton = view.findViewById(R.id.organizerNotifications);
        cameraButton = view.findViewById(R.id.buttonCamera);
        guideButton = view.findViewById(R.id.buttonGuide);
        filterButton = view.findViewById(R.id.buttonFilter);
        createButton = view.findViewById(R.id.buttonCreate);
        notifyButton = view.findViewById(R.id.buttonNotify);
        eventHistoryButton = view.findViewById(R.id.buttonEventHistory);
        myProfileButton = view.findViewById(R.id.buttonMyProfile);
        hostEventList = view.findViewById(R.id.HostEventView);

        hostEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        hostEventList.setAdapter(eventArrayAdapter);

        loadEvents();
        setUpClickListeners();
    }
    private void loadEvents() {
        String fromDate = DateValidation.getCurrentDate();

        LocalDate futureDate = LocalDate.now().plusYears(1);
        String toDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (DateValidation.dateRangeValid(fromDate, toDate)) {
            eventService.listUpcoming(fromDate, toDate, null)
                    .addOnSuccessListener(events -> {
                        if (eventsArrayList != null) {
                            eventsArrayList.clear();
                            eventsArrayList.addAll(events);
                            eventArrayAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void setUpClickListeners() {
        notificationsButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(OrganizerHomeFragment.this)
                    .navigate(R.id.action_OrganizerHomeFragment_to_NotificationsFragment);
        });

        myProfileButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(OrganizerHomeFragment.this)
                    .navigate(R.id.action_OrganizerHomeFragment_to_OrganizerProfileFragment);
        });

        createButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(OrganizerHomeFragment.this)
                    .navigate(R.id.action_OrganizerHomeFragment_to_CreateEventFragment);
                });

        // Temporary toast messages for unimplemented features
        cameraButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Camera feature not implemented yet", Toast.LENGTH_SHORT).show());

        guideButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Guide feature not implemented yet", Toast.LENGTH_SHORT).show());

        buttonCreate.setOnClickListener(v ->
                NavHostFragment.findNavController(OrganizerHomeFragment.this)
                        .navigate(R.id.action_OrganizerHomeFragment_to_HostCreateEventFragment)
        );


        notifyButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Notify feature not implemented yet", Toast.LENGTH_SHORT).show());

        eventHistoryButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Event History not implemented yet", Toast.LENGTH_SHORT).show());
    }
}
