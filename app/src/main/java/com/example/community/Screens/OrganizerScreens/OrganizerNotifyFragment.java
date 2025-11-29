package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.EventArrayAdapter;
import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.UserService;

import java.util.ArrayList;

public class OrganizerNotifyFragment extends Fragment {

    private RecyclerView notifyEventList;
    private Button backButton;

    private EventArrayAdapter eventArrayAdapter;
    private ArrayList<Event> eventsArrayList;
    private String currentOrganizerID;

    private EventService eventService;
    private UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_notify_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventService = new EventService();
        userService = new UserService();
        eventsArrayList = new ArrayList<>();

        notifyEventList = view.findViewById(R.id.notifyEventRecyclerView);
        backButton = view.findViewById(R.id.buttonBack);

        notifyEventList.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        eventArrayAdapter.setOnEventClickListener(event -> {
            NotificationTargetDialogFragment dialogFragment = NotificationTargetDialogFragment.newInstance(event.getEventID());
            dialogFragment.show(getChildFragmentManager(), "notification_target");
        });


        notifyEventList.setAdapter(eventArrayAdapter);

        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        loadOrganizerData();


    }

    private void loadOrganizerData() {
        String deviceToken = userService.getDeviceToken();
        userService.getByDeviceToken(deviceToken)
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                    currentOrganizerID = user.getUserID();
                    loadEvents();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load organizer data", Toast.LENGTH_SHORT).show();
                });
    }
    private void loadEvents() {

        eventService.listEventsByOrganizer(currentOrganizerID, 100, null)
                .addOnSuccessListener(events -> {
                    if (eventsArrayList != null) {
                        eventsArrayList.clear();
                        eventsArrayList.addAll(events);
                        eventArrayAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
                });
    }
}

