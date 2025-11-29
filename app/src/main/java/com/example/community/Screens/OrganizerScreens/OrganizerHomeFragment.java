package com.example.community.Screens.OrganizerScreens;

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
import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.UserService;

import java.util.ArrayList;

/**
 * Fragment representing the Organizer Home screen.
 * Displays the list of events hosted by the current organizer and provides navigation
 * to features such as creating events, notifications, profile, and other organizer actions.
 */
public class OrganizerHomeFragment extends Fragment {

    /** UI Buttons */
    private ImageButton notificationsButton, cameraButton;
    private Button guideButton, filterButton, createButton, notifyButton;
    private Button eventHistoryButton, myProfileButton;

    /** RecyclerView displaying the organizer's events */
    private RecyclerView hostEventList;

    /** Data for the events */
    private ArrayList<Event> eventsArrayList;
    private EventArrayAdapter eventArrayAdapter;

    /** Current organizer ID */
    private String currentOrganizerID;

    /** Services for interacting with events and users */
    private EventService eventService;
    private UserService userService;

    /**
     * Inflates the fragment layout.
     *
     * @param inflater LayoutInflater to inflate the view
     * @param container Parent container
     * @param savedInstanceState Previously saved state
     * @return Root view of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.host_main_page, container, false);
    }

    /**
     * Initializes UI elements, sets up RecyclerView and adapter, and loads organizer data.
     *
     * @param view Root view of the fragment
     * @param savedInstanceState Previously saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventService = new EventService();
        userService = new UserService();
        eventsArrayList = new ArrayList<>();

        // Initialize UI components
        notificationsButton = view.findViewById(R.id.organizerNotifications);
        cameraButton = view.findViewById(R.id.buttonCamera);
        guideButton = view.findViewById(R.id.buttonGuide);
        filterButton = view.findViewById(R.id.buttonFilter);
        createButton = view.findViewById(R.id.buttonCreate);
        notifyButton = view.findViewById(R.id.buttonNotify);
        eventHistoryButton = view.findViewById(R.id.buttonEventHistory);
        myProfileButton = view.findViewById(R.id.buttonMyProfile);
        hostEventList = view.findViewById(R.id.HostEventView);

        // Set up RecyclerView
        hostEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventArrayAdapter = new EventArrayAdapter(eventsArrayList);
        eventArrayAdapter.setOnEventClickListener(event -> {
            Bundle args = new Bundle();
            args.putString("event_id", event.getEventID());
            NavHostFragment.findNavController(OrganizerHomeFragment.this)
                    .navigate(R.id.action_OrganizerHomeFragment_to_OrganizerEventDescriptionFragment, args);
        });
        hostEventList.setAdapter(eventArrayAdapter);

        loadOrganizerData();
        setUpClickListeners();
    }

    /**
     * Loads current organizer data using device token, then triggers loading of their events.
     */
    private void loadOrganizerData() {
        String deviceToken = userService.getDeviceToken();
        userService.getByDeviceToken(deviceToken)
                .addOnSuccessListener(user -> {
                    if (user == null) {
                        Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    currentOrganizerID = user.getUserID();
                    loadEvents();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load organizer data", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Loads events hosted by the current organizer and updates the RecyclerView.
     */
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

    /**
     * Sets up click listeners for all buttons in the fragment.
     * Includes navigation and placeholder toasts for unimplemented features.
     */
    private void setUpClickListeners() {
        notificationsButton.setOnClickListener(v ->
                NavHostFragment.findNavController(OrganizerHomeFragment.this)
                        .navigate(R.id.action_OrganizerHomeFragment_to_NotificationsFragment));

        myProfileButton.setOnClickListener(v ->
                NavHostFragment.findNavController(OrganizerHomeFragment.this)
                        .navigate(R.id.action_OrganizerHomeFragment_to_OrganizerProfileFragment));

        createButton.setOnClickListener(v ->
                NavHostFragment.findNavController(OrganizerHomeFragment.this)
                        .navigate(R.id.action_OrganizerHomeFragment_to_CreateEventFragment));

        cameraButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Camera feature not implemented yet", Toast.LENGTH_SHORT).show());

        guideButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Guide feature not implemented yet", Toast.LENGTH_SHORT).show());

        notifyButton.setOnClickListener(v ->
                NavHostFragment.findNavController(OrganizerHomeFragment.this)
                        .navigate(R.id.action_OrganizerHomeFragment_to_HostNotifyFragment));

        eventHistoryButton.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Event History not implemented yet", Toast.LENGTH_SHORT).show());
    }
}
