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

public class OrganizerHomeFragment extends Fragment {

    // toolbar
    private ImageButton buttonNotification, buttonCamera;
    private Button buttonGuide, buttonFilter;

    // main
    private RecyclerView hostEventView;
    private Button buttonCreate, buttonNotify;

    // footer
    private Button buttonEventHistory, buttonMyProfile;

    // data
    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;
    private EventService eventService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.host_main_page, container, false);
        // ^^^ use your file name here
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bind views
        buttonNotification = view.findViewById(R.id.buttonNotification);
        buttonCamera       = view.findViewById(R.id.buttonCamera);
        buttonGuide        = view.findViewById(R.id.buttonGuide);
        buttonFilter       = view.findViewById(R.id.buttonFilter);

        hostEventView      = view.findViewById(R.id.HostEventView);
        buttonCreate       = view.findViewById(R.id.buttonCreate);
        buttonNotify       = view.findViewById(R.id.buttonNotify);

        buttonEventHistory = view.findViewById(R.id.buttonEventHistory);
        buttonMyProfile    = view.findViewById(R.id.buttonMyProfile);

        // recycler + data
        eventService = new EventService();
        eventsArrayList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);

        hostEventView.setLayoutManager(new LinearLayoutManager(getContext()));
        hostEventView.setAdapter(eventArrayAdapter);

        loadEvents();
        setClicks();
    }

    private void loadEvents() {
        String fromDate = DateValidation.getCurrentDate();
        String toDate = LocalDate.now().plusYears(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (DateValidation.dateRangeValid(fromDate, toDate)) {
            eventService.listUpcoming(fromDate, toDate, null)
                    .addOnSuccessListener(events -> {
                        eventsArrayList.clear();
                        eventsArrayList.addAll(events);
                        eventArrayAdapter.notifyDataSetChanged();
                    });
        }
    }

    private void setClicks() {
        buttonNotification.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Notifications", Toast.LENGTH_SHORT).show());

        buttonCamera.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Open Camera", Toast.LENGTH_SHORT).show());

        buttonGuide.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Guide", Toast.LENGTH_SHORT).show());

        buttonFilter.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Filter", Toast.LENGTH_SHORT).show());

        buttonCreate.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Create+", Toast.LENGTH_SHORT).show());

        buttonNotify.setOnClickListener(v ->
                NavHostFragment.findNavController(OrganizerHomeFragment.this)
                        .navigate(R.id.action_OrganizerHomeFragment_to_HostNotifyFragment)
        );

        buttonEventHistory.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Event History", Toast.LENGTH_SHORT).show());

        buttonMyProfile.setOnClickListener(v ->
                Toast.makeText(getActivity(), "My Profile", Toast.LENGTH_SHORT).show());
    }
}
